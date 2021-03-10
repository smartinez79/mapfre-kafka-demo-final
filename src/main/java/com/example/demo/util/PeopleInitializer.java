package com.example.demo.util;

import com.example.demo.data.Address;
import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class PeopleInitializer {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PeopleInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void initialize() {
        if (!mongoTemplate.collectionExists("people")) {
            mongoTemplate.insert(createSamplePerson("JUAN", "DIAZ", "C/LA PARROCHA, 23", "GIJON", "ASTURIAS", "33305", Collections.singletonList("JUANELE")));
            mongoTemplate.insert(createSamplePerson("PEDRO", "HERNANDEZ", "C/CALATAYUD, 10", "MADRID", "MADRID", "28080", Collections.singletonList("PEDRITO")));
            mongoTemplate.insert(createSamplePerson("SEBASTIAN", "GARCIA", "C/PEDRERA, S/N", "BARCELONA", "BARCELONA", "08080", Collections.singletonList("SEBAS")));
        }
    }

    private Person createSamplePerson(String firstName, String lastName, String street, String city,
                                      String state, String zip, List<String> aliases) {
        return Person.builder()
                .id(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .address(Address.builder()
                        .street(street)
                        .city(city)
                        .state(state)
                        .zip(zip)
                        .build())
                .aliases(aliases)
                .build();
    }
}
