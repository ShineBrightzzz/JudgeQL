package com.hainam.judgeql.submission.service;

import java.util.UUID;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hainam.judgeql.grading.config.RabbitConstants;
import com.hainam.judgeql.grading.service.GradingService;

/**
 * Service to send submission IDs to the grading queue
 */
@Service
public class SubmissionProducer {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private GradingService gradingService;
    
    /**
     * Sends a submission to the grading queue for processing
     * Falls back to direct processing if RabbitMQ is unavailable
     * 
     * @param submissionId The UUID of the submission to process
     */
    public void sendToGradingQueue(UUID submissionId) {
        System.out.println("📤 Sending submission to grading queue: " + submissionId);
        
        try {
            // Try to send to RabbitMQ
            rabbitTemplate.convertAndSend(
                RabbitConstants.ORDER_EXCHANGE, 
                RabbitConstants.ORDER_ROUTING_KEY, 
                submissionId
            );
            System.out.println("✅ Submission sent to queue: " + submissionId);
        } catch (AmqpConnectException e) {
            // RabbitMQ is unavailable, process directly as fallback
            handleRabbitMQFailure(submissionId, e, "Connection to RabbitMQ failed");
        } catch (AmqpException e) {
            // Other RabbitMQ errors
            handleRabbitMQFailure(submissionId, e, "RabbitMQ error occurred");
        }
    }
    
    /**
     * Handles RabbitMQ failures by processing the submission directly
     */
    private void handleRabbitMQFailure(UUID submissionId, Exception e, String message) {
        System.err.println("❌ " + message + ": " + e.getMessage());
        System.out.println("⚠️ Falling back to direct processing for submission: " + submissionId);
        
        // Process submission in a separate thread to not block the request
        new Thread(() -> {
            try {
                gradingService.processSubmission(submissionId);
                System.out.println("✅ Direct processing completed for submission: " + submissionId);
            } catch (Exception ex) {
                System.err.println("❌ Error in direct processing: " + ex.getMessage());
                ex.printStackTrace();
            }
        }).start();
    }
}
