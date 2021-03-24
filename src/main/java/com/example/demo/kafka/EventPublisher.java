
package com.example.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.events.DemoEvent;

@Service
public class EventPublisher {
	private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired(required = false)
	private KafkaTopics topics;

	public boolean publishEvent(final DemoEvent event) {
		log.info("Publishing event {}", event);
		
		return kafkaTemplate.executeInTransaction(t -> {
			t.send(topics.getEvents().getName(), event.getId(), event);
			return true;
		});
	}

}

