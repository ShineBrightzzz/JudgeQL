package com.hainam.judgeql.grading.service;

import java.util.UUID;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hainam.judgeql.grading.config.RabbitConstants;

@Service
public class GradingConsumer {
    
    @Autowired
    private GradingService gradingService;

    /**
     * Handles submission messages from the queue
     * 
     * @param submissionId The UUID of the submission to grade
     */
    @RabbitListener(queues = RabbitConstants.ORDER_QUEUE)
    public void handleSubmission(UUID submissionId) {
        try {
            System.out.println("⏳ Grading submission: " + submissionId);
            gradingService.processSubmission(submissionId);
            System.out.println("✅ Finished grading submission: " + submissionId);
        } catch (Exception e) {
            System.err.println("❌ Error grading submission: " + submissionId);
            e.printStackTrace();
            // Don't requeue the message if it's a permanent failure
            throw new AmqpRejectAndDontRequeueException("Failed to process submission", e);
        }
    }
}
