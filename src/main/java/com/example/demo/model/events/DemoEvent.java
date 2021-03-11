package com.example.demo.model.events;

import com.example.demo.model.Person;

import lombok.Data;

@Data
public class DemoEvent {

	public enum EventType { INSERT };
	
	private final Person person;
	private final EventType eventType;
	private final String id;

}
