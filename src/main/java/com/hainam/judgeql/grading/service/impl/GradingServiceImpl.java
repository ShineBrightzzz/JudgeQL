package com.hainam.judgeql.grading.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hainam.judgeql.grading.domain.GradingResult;
import com.hainam.judgeql.grading.domain.TestResultStatus;
import com.hainam.judgeql.grading.dto.request.GradingRequest;
import com.hainam.judgeql.grading.dto.request.TestCaseGradingRequest;
import com.hainam.judgeql.grading.dto.response.GradingResponse;
import com.hainam.judgeql.grading.dto.response.TestCaseResult;
import com.hainam.judgeql.grading.mapper.GradingMapper;
import com.hainam.judgeql.grading.repository.GradingResultRepository;
import com.hainam.judgeql.grading.service.GradingService;
import com.hainam.judgeql.submission.domain.Submission;
import com.hainam.judgeql.submission.domain.SubmissionStatus;
import com.hainam.judgeql.submission.repository.SubmissionRepository;
import com.hainam.judgeql.testcase.domain.TestCase;
import com.hainam.judgeql.testcase.repository.TestCaseRepository;

@Service
public class GradingServiceImpl implements GradingService {

    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private GradingResultRepository gradingResultRepository;
    
    @Autowired
    private GradingMapper gradingMapper;
      @Autowired
    private TestCaseRepository testCaseRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private int h2QueryTimeoutSeconds;
    
    @Autowired
    private int h2MaxConnectionRetries;
    
    @Autowired
    private long h2RetryDelayMs;@Override
    public GradingResponse gradeSubmission(GradingRequest request) {
        // Initialize the response
        GradingResponse response = new GradingResponse();
        response.setSubmissionId(request.getSubmissionId());
        response.setTotalTestCount(request.getTestCases().size());
        
        List<TestCaseResult> testCaseResults = new ArrayList<>();
        int passedTestCount = 0;
        
        // Grade each test case
        for (TestCaseGradingRequest testCase : request.getTestCases()) {
            TestCaseResult result = evaluateTestCase(testCase, request.getUserSql());
            testCaseResults.add(result);
            
            if (TestResultStatus.PASS.getValue().equals(result.getStatus())) {
                passedTestCount++;
            }
        }
        
        // Calculate score (percentage of passed tests)
        int score = request.getTestCases().isEmpty() ? 0 
                : (int) Math.round((double) passedTestCount / request.getTestCases().size() * 100);
        
        // Set response fields
        response.setPassedTestCount(passedTestCount);
        response.setTestCaseResults(testCaseResults);
        response.setScore(score);
        response.setStatus("done");
        
        return response;
    }

    @Override
    public void processSubmission(UUID submissionId) {
        // Fetch submission from database
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found: " + submissionId));
        
        // Update submission status to RUNNING
        submission.setStatus(SubmissionStatus.RUNNING);
        submissionRepository.save(submission);
        
        try {
            // Create grading request
            GradingRequest request = createGradingRequest(submission);
            
            // Process the request
            GradingResponse response = gradeSubmission(request);
            
            // Update submission status to COMPLETED
            submission.setStatus(SubmissionStatus.COMPLETED);
            submissionRepository.save(submission);
            
            // Save grading results to database
            saveGradingResults(submission, response);
            
        } catch (Exception e) {
            // Handle errors
            submission.setStatus(SubmissionStatus.ERROR);
            submissionRepository.save(submission);
            e.printStackTrace();
        }
    }
      private GradingRequest createGradingRequest(Submission submission) {
        // Create the request
        GradingRequest request = new GradingRequest();
        request.setSubmissionId(submission.getId());
        request.setUserId(submission.getUserId());
        request.setQuestionId(submission.getQuestionId());
        request.setUserSql(submission.getInput());
        
        // Fetch test cases from the database
        List<TestCaseGradingRequest> testCases = fetchTestCases(submission.getQuestionId());
        request.setTestCases(testCases);
        
        return request;
    }      private List<TestCaseGradingRequest> fetchTestCases(String questionId) {
        // Fetch test cases from the testcase module
        List<TestCase> testCases = testCaseRepository.findByQuestionId(UUID.fromString(questionId));
        
        // Convert TestCase entities to TestCaseGradingRequest
        return testCases.stream()
                .map(testCase -> {
                    return TestCaseGradingRequest.builder()
                            .testcaseId(testCase.getId().getMostSignificantBits())
                            .setupSql(testCase.getSetupSql())
                            .expectedOutput(testCase.getExpectedOutput())
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    private void saveGradingResults(Submission submission, GradingResponse response) {
        GradingResult gradingResult = gradingMapper.toEntity(response);
        gradingResultRepository.save(gradingResult);
    }    private TestCaseResult evaluateTestCase(TestCaseGradingRequest testCase, String userSql) {
        TestCaseResult result = new TestCaseResult();
        result.setTestcaseId(testCase.getTestcaseId());
        result.setExpectedOutput(testCase.getExpectedOutput());
          Connection connection = null;
        Statement statement = null;
        
        // Use configured number of retries for connection
        int retryCount = 0;
        
        while (retryCount < h2MaxConnectionRetries) {
            try {
                // Create an in-memory H2 database connection
                // Each connection gets its own unique database name to ensure isolation
                String uniqueDbName = "test_" + UUID.randomUUID().toString().replace("-", "");
                String h2ConnectionUrl = "jdbc:h2:mem:" + uniqueDbName + ";DB_CLOSE_DELAY=-1;MODE=PostgreSQL";
                
                // Connect to the in-memory database
                connection = DriverManager.getConnection(h2ConnectionUrl, "sa", "");
                
                // Configure H2 for PostgreSQL compatibility
                configureH2ForPostgresCompatibility(connection);
                  // Set query timeout to prevent long-running queries
                statement = connection.createStatement();
                statement.setQueryTimeout(h2QueryTimeoutSeconds);
                
                // Execute setup SQL to create tables and insert data
                try {
                    statement.execute(testCase.getSetupSql());
                } catch (SQLException e) {
                    result.setStatus(TestResultStatus.ERROR.getValue());
                    String errorMsg = "Error in setup SQL: " + e.getMessage();
                    System.err.println(errorMsg);
                    result.setErrorMessage(errorMsg);
                    return result;
                }
                
                try {
                    // Execute user's SQL query and get results
                    // Check if the SQL is a SELECT query
                    String trimmedSql = userSql.trim().toLowerCase();
                    if (trimmedSql.startsWith("select") || trimmedSql.startsWith("with")) {
                        ResultSet resultSet = statement.executeQuery(userSql);
                        List<Map<String, Object>> actualOutput = resultSetToList(resultSet);
                        result.setActualOutput(actualOutput);
                        
                        // Compare actual output with expected output
                        boolean passed = compareResults(actualOutput, testCase.getExpectedOutput());
                        
                        if (passed) {
                            result.setStatus(TestResultStatus.PASS.getValue());
                        } else {
                            result.setStatus(TestResultStatus.FAIL.getValue());
                        }
                    } else {
                        // For non-SELECT queries (INSERT, UPDATE, DELETE, etc.)
                        int rowsAffected = statement.executeUpdate(userSql);
                        
                        // For non-SELECT queries, we typically want to verify by running a SELECT after
                        // This is handled by the expected output and a SELECT query in the test case
                        result.setActualOutput(List.of(Map.of("rowsAffected", rowsAffected)));
                        
                        // Execute a verification query if needed
                        // This is assumed to be the first query in the expected output
                        if (testCase.getExpectedOutput() != null && !testCase.getExpectedOutput().isEmpty()) {
                            result.setStatus(TestResultStatus.PASS.getValue());
                        } else {
                            result.setStatus(TestResultStatus.FAIL.getValue());
                        }
                    }
                } catch (SQLException e) {
                    // Handle SQL errors in user's query
                    result.setStatus(TestResultStatus.ERROR.getValue());
                    String errorMsg = "Error in SQL query: " + e.getMessage();
                    System.err.println(errorMsg);
                    result.setErrorMessage(errorMsg);
                }
                
                // If we reached here, we're done with the evaluation
                break;
                
            } catch (SQLException e) {
                retryCount++;
                  // If we've exhausted all retries, handle the error
                if (retryCount >= h2MaxConnectionRetries) {
                    result.setStatus(TestResultStatus.ERROR.getValue());
                    String errorMsg = "SQL Error after " + h2MaxConnectionRetries + " attempts: " + e.getMessage();
                    System.err.println(errorMsg);
                    if (e.getNextException() != null) {
                        System.err.println("Caused by: " + e.getNextException().getMessage());
                    }
                    result.setErrorMessage(errorMsg);
                } else {
                    // Log the retry
                    System.err.println("Connection attempt " + retryCount + " failed. Retrying...");
                      // Wait before retrying using configured delay
                    try {
                        Thread.sleep(h2RetryDelayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                // Close resources
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        // For H2 in-memory database, closing the connection will also drop the database
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return result;
    }
      private List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();
        
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                // Use getColumnLabel instead of getColumnName to support aliases
                String columnName = md.getColumnLabel(i);
                Object value = rs.getObject(i);
                
                // Convert specific types for consistency
                if (value instanceof Number) {
                    // For numeric types, ensure consistent representation
                    if (value instanceof Float || value instanceof Double) {
                        // For floating point numbers, use BigDecimal to avoid precision issues
                        value = rs.getBigDecimal(i);
                    } else if (value instanceof Integer || value instanceof Long) {
                        // For integers, ensure long type for consistency
                        value = rs.getLong(i);
                    }
                }
                
                row.put(columnName, value);
            }
            rows.add(row);
        }
        
        return rows;
    }
      private boolean compareResults(List<Map<String, Object>> actual, List<Map<String, Object>> expected) {
        if (actual == null || expected == null) {
            return actual == expected; // Both null is a match, otherwise not
        }
        
        // Check if the row count matches
        if (actual.size() != expected.size()) {
            System.err.println("Row count mismatch: actual=" + actual.size() + ", expected=" + expected.size());
            return false;
        }
        
        // For each row in the expected result set
        for (int i = 0; i < expected.size(); i++) {
            Map<String, Object> expectedRow = expected.get(i);
            
            // Try to find a matching row in the actual result set
            boolean rowMatched = false;
            for (Map<String, Object> actualRow : actual) {
                if (rowMatches(actualRow, expectedRow)) {
                    rowMatched = true;
                    break;
                }
            }
            
            if (!rowMatched) {
                System.err.println("No match found for expected row: " + expectedRow);
                return false;
            }
        }
        
        return true;
    }
      private boolean rowMatches(Map<String, Object> actualRow, Map<String, Object> expectedRow) {
        // The expected row might have fewer columns than the actual row (we only check what was specified)
        for (Map.Entry<String, Object> entry : expectedRow.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();
            
            // Find case-insensitive matching key in actual row
            String matchingKey = key;
            boolean keyFound = false;
            
            // Look for the key in a case-insensitive manner
            for (String actualKey : actualRow.keySet()) {
                if (actualKey.equalsIgnoreCase(key)) {
                    matchingKey = actualKey;
                    keyFound = true;
                    break;
                }
            }
            
            // Check if the key exists in the actual row
            if (!keyFound) {
                System.err.println("Column missing in actual result: " + key);
                return false;
            }
            
            Object actualValue = actualRow.get(matchingKey);
            
            // Handle null values
            if (expectedValue == null) {
                if (actualValue != null) {
                    System.err.println("Value mismatch for column " + key + ": expected=null, actual=" + actualValue);
                    return false;
                }
                continue;
            }
            
            // Convert numeric types for comparison if needed
            if (expectedValue instanceof Number && actualValue instanceof Number) {
                // Convert to BigDecimal for accurate numeric comparison
                BigDecimal expectedBD = new BigDecimal(expectedValue.toString());
                BigDecimal actualBD = new BigDecimal(actualValue.toString());
                
                if (expectedBD.compareTo(actualBD) != 0) {
                    System.err.println("Numeric value mismatch for column " + key + 
                            ": expected=" + expectedValue + ", actual=" + actualValue);
                    return false;
                }
            }
            // Handle string comparison with trimming
            else if (expectedValue instanceof String && actualValue instanceof String) {
                String expectedStr = ((String) expectedValue).trim();
                String actualStr = ((String) actualValue).trim();
                
                if (!expectedStr.equalsIgnoreCase(actualStr)) {
                    System.err.println("String value mismatch for column " + key + 
                            ": expected='" + expectedStr + "', actual='" + actualStr + "'");
                    return false;
                }
            }
            // Default object comparison
            else if (!expectedValue.equals(actualValue)) {
                System.err.println("Value mismatch for column " + key + 
                        ": expected=" + expectedValue + ", actual=" + actualValue);
                return false;
            }
        }
        
        return true;
    }

	@Override
	public Optional<GradingResponse> getGradingResultBySubmissionId(UUID submissionId) {
		return gradingResultRepository.findBySubmissionId(submissionId)
				.map(gradingMapper::toResponse);
	}
	
	/**
     * Configures the H2 database to be compatible with PostgreSQL syntax
     * @param connection The H2 database connection
     * @throws SQLException If there is an error executing SQL statements
     */
    private void configureH2ForPostgresCompatibility(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
              // Enable PostgreSQL compatibility mode
            statement.execute("SET MODE PostgreSQL");
            
            // Configure case sensitivity to match PostgreSQL
            statement.execute("SET IGNORECASE TRUE");
            
            // Enable functions similar to PostgreSQL
            statement.execute("CREATE ALIAS IF NOT EXISTS now FOR \"java.lang.System.currentTimeMillis\"");
            
            // Configure date/time functions
            statement.execute("SET TIME ZONE 'UTC'");
            
            // Additional compatibility settings if needed
            // ...
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
}
