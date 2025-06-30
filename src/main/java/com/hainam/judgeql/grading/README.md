# SQL Grading System - JudgeQL

## Overview

The SQL Grading System is a core component of JudgeQL that evaluates SQL submissions against test cases. It executes user-submitted SQL queries in an isolated environment and compares the results with expected outputs.

## Key Features

- **Isolated Execution Environment**: Uses H2 in-memory database to execute SQL queries safely without affecting the production database
- **Flexible Test Case Support**: Supports various SQL operations (SELECT, INSERT, UPDATE, DELETE)
- **Detailed Result Reporting**: Provides detailed feedback on test case execution
- **PostgreSQL Compatibility Mode**: Configures H2 to be compatible with PostgreSQL syntax

## Architecture

The grading system consists of the following components:

1. **GradingService**: Core service for processing submissions and evaluating test cases
2. **H2 In-Memory Database**: Provides isolated execution environment for SQL queries
3. **TestCase Integration**: Fetches test cases from the testcase module
4. **Result Reporting**: Returns detailed results for each test case

## Usage

### Processing a Submission

```java
// Process a submission
UUID submissionId = UUID.fromString("submission-uuid");
gradingService.processSubmission(submissionId);

// Get grading results
Optional<GradingResponse> result = gradingService.getGradingResultBySubmissionId(submissionId);
```

### Test Case Structure

Each test case consists of:

1. **Setup SQL**: SQL statements to create tables and insert test data
2. **Expected Output**: The expected result of the user's SQL query
3. **Test Case ID**: Unique identifier for the test case

### Grading Process

1. For each test case:
   - Create a new H2 in-memory database with a unique name
   - Execute setup SQL to create the test environment
   - Execute the user's SQL query
   - Compare the results with the expected output
   - Record the test case status (PASS, FAIL, or ERROR)

2. Calculate the final score based on the percentage of passed tests

## Configuration

The H2 database can be configured via the `H2DatabaseConfig` class:

- `h2QueryTimeoutSeconds`: Maximum time allowed for a query execution (default: 30 seconds)
- `h2MaxConnectionRetries`: Maximum number of connection retry attempts (default: 3)
- `h2RetryDelayMs`: Delay between connection retry attempts (default: 1000ms)

## Utilities

The `SqlTestUtil` class provides utilities for testing SQL queries manually:

```java
// Test a SQL query
String setupSql = "CREATE TABLE test (id INT); INSERT INTO test VALUES (1), (2), (3)";
String querySql = "SELECT * FROM test WHERE id > 1";
List<Map<String, Object>> results = SqlTestUtil.testSqlQuery(setupSql, querySql);

// Print the results
SqlTestUtil.printResults(results);
```

## Error Handling

The system handles various error conditions:

1. **SQL Syntax Errors**: Reported with detailed error messages
2. **Connection Issues**: Automatic retry with configurable attempts
3. **Query Timeout**: Long-running queries are terminated to prevent resource exhaustion
4. **Result Comparison Errors**: Detailed reporting of differences between actual and expected results
