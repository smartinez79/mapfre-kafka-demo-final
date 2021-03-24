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

	public void setPerson(Person person) {
		this.person = person;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DemoEvent{" +
				"person=" + person +
				", eventType=" + eventType +
				", id='" + id + '\'' +
				'}';
	}
}
