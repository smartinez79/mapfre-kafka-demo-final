package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.kafka.EventListener;
import com.example.demo.model.events.DemoEvent;

@Service
public class EventService {
	@Autowired
	private EventListener eventListener;
	
	
	public List<DemoEvent> getLatestEvents() {
		return eventListener.getQueuedEvents();
	}
}
