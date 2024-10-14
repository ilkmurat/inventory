package com.murat.oneamz.inventory.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventService {

    private static final String TOPIC = "inventory-events";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public InventoryEventService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Method to publish an inventory event
    public void publishEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
    }

    // Listener method for consuming messages
    @KafkaListener(topics = TOPIC, groupId = "inventory-group")
    public void listen(String message) {
        // Handle the incoming message
        System.out.println("Received message: " + message);
        // Add logic to process the message as needed
    }
}
