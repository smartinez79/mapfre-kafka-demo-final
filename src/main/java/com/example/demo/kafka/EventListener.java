package com.example.demo.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.demo.model.events.DemoEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnProperty("kafka.bootstrap-servers")
public class EventListener {
	private final static Logger log = LoggerFactory.getLogger(EventListener.class);
	private final static int CAPACITY = 100;

	private Queue<DemoEvent> events;

	public EventListener() {
		events = new ArrayBlockingQueue<DemoEvent>(CAPACITY);
	}

	@KafkaListener(topics = "demo.course.actions", containerFactory = "demoEventListenerFactory")
	public void listenAsObject(@Payload DemoEvent event) {

		log.info("Reading demo event from topic: {}", event);

		if (events.offer(event)) {
			log.info("New event stored in the queue");
		} else {
			log.info("Queue is full. Dropping event");
		}
	}

	public List<DemoEvent> getQueuedEvents() {
		List<DemoEvent> queuedEvents= new ArrayList<>();
		DemoEvent oldestEvent;
		
		while( (oldestEvent = events.poll() ) != null) {
			queuedEvents.add(oldestEvent);
		}

		log.info("There was {} events in the queue", queuedEvents.size());

		return queuedEvents;
	}
}
