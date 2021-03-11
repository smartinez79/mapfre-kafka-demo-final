package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.events.DemoEvent;
import com.example.demo.service.EventService;

@RestController
@RequestMapping("/api")
public class EventController {
	@Autowired
	private EventService eventService;
	
    @RequestMapping(path = "/events", method = RequestMethod.GET)
    public ResponseEntity<List<DemoEvent>> getLatestEvents() {
        List<DemoEvent> events = eventService.getLatestEvents();
        return ResponseEntity.ok(events);
    }
}
