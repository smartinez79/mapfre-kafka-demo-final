package com.example.demo.controller;

import com.example.demo.data.PersonDto;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // Find all people / Find people by first name
    @RequestMapping(path = "/people", method = RequestMethod.GET)
    public ResponseEntity<List<PersonDto>> findPeople(
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "city", required = false) String city) {
        List<PersonDto> people = new ArrayList<>();
        if (firstName == null && city == null) {
            people = personService.findAll();
        }
        if (firstName != null && city == null) {
            people = personService.findByFirstName(firstName);
        }
        if (firstName == null && city != null) {
            people = personService.findByCity(city);
        }
        if (firstName != null && city != null) {
            people = personService.findByFirstNameAndCity(firstName, city);
        }
        return people.isEmpty() ?
                // 204 No Content
                ResponseEntity.noContent().build() :
                // 200 OK
                ResponseEntity.ok(people);
    }

    // Find people by id
    @RequestMapping(path = "/people/{id}", method = RequestMethod.GET)
    public ResponseEntity<PersonDto> findPersonById(@PathVariable("id") String id) {
        PersonDto personDto = personService.findById(id);
        return personDto == null ?
                // 404 Not Found
                ResponseEntity.notFound().build() :
                // 200 OK
                ResponseEntity.ok(personDto);
    }

    // Create person
    @RequestMapping(path = "/people", method = RequestMethod.POST)
    public ResponseEntity<?> createPerson(@RequestBody PersonDto personDto) {
        PersonDto persistedPerson = personService.createPerson(personDto);
        try {
            // 201 Created
            return ResponseEntity.created(new URI("/api/people/" + persistedPerson.getId())).build();
        } catch (URISyntaxException ex) {
            // 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // Update person
    @RequestMapping(path = "/people/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePerson(@PathVariable("id") String id,
                                          @RequestBody PersonDto personDto) {
        PersonDto updatedPerson = personService.updatePerson(id, personDto);
        return updatedPerson == null ?
                // 404 Not Found
                ResponseEntity.notFound().build() :
                // 200 OK
                ResponseEntity.ok(updatedPerson);
    }

    // Update person aliases
    @RequestMapping(path = "/people/{id}/aliases", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePersonAliases(@PathVariable("id") String id,
                                                 @RequestBody List<String> aliases) {
        PersonDto updatedPerson = personService.updatePersonAliases(id, aliases);
        return updatedPerson == null ?
                // 404 Not Found
                ResponseEntity.notFound().build() :
                // 200 OK
                ResponseEntity.ok(updatedPerson);
    }

    // Delete person
    @RequestMapping(path = "/people/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePerson(@PathVariable String id) {
        boolean wasDeleted = personService.deletePersonById(id);
        return !wasDeleted ?
                // 404 Not Found
                ResponseEntity.notFound().build() :
                // 204 No content
                ResponseEntity.noContent().build();
    }

    // Delete all people
    @RequestMapping(path = "/people", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllPeople() {
        personService.deleteAllPeople();
        // 204 No content
        return ResponseEntity.noContent().build();
    }
}
