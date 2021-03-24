package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.kafka.EventListener;
import com.example.demo.model.events.DemoEvent;

@Service
public class EventService {
	private static final Logger log = LoggerFactory.getLogger(EventService.class);
	@Autowired
	private EventListener eventListener;
	
	
	public List<DemoEvent> getLatestEvents() {
		log.info("Getting latest events");
		return eventListener.getQueuedEvents();
	}
}
