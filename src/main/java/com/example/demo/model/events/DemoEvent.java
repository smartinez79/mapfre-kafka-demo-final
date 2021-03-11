package com.example.demo.model.events;

import java.util.UUID;

import com.example.demo.model.Person;

import lombok.Data;

@Data
public class DemoEvent {
	public enum EventType { INSERT, UPDATE, DELETE };
	
	private final Person person;
	private final EventType eventType;
	private final String id;
	
	public DemoEvent(Person person, EventType type) {
		this(person, type, UUID.randomUUID().toString());
	}

	public DemoEvent(Person person, EventType type, String uuid) {
		this.person= person;
		this.eventType= type;
		this.id= uuid;
	}

}
