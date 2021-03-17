package com.example.demo.model.events;

import java.util.UUID;

import com.example.demo.model.Person;

import lombok.Data;

public class DemoEvent {
	public enum EventType { INSERT, UPDATE, DELETE };
	
	private Person person;
	private EventType eventType;
	private String id;

	public DemoEvent() {
	}

	public DemoEvent(Person person, EventType type) {
		this(person, type, UUID.randomUUID().toString());
	}

	public DemoEvent(Person person, EventType type, String uuid) {
		this.person= person;
		this.eventType= type;
		this.id= uuid;
	}

	public Person getPerson() {
		return person;
	}

	public EventType getEventType() {
		return eventType;
	}

	public String getId() {
		return id;
	}
}
