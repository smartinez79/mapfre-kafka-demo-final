
package com.example.demo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.events.DemoEvent;

@Service
public class EventPublisher {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired(required = false)
	private KafkaTopics topics;

	public boolean publishEvent(final DemoEvent event) {
		return kafkaTemplate.executeInTransaction(t -> {
			t.send(topics.getEvents().getName(), event.getId(), event);
			return true;
		});
	}

}

