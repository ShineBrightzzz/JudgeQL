package com.hainam.judgeql.grading.config;

/**
 * Constants for RabbitMQ configuration
 */
public class RabbitConstants {
    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String ORDER_QUEUE = "order-queue";
    public static final String ORDER_ROUTING_KEY = "order.#";
}
