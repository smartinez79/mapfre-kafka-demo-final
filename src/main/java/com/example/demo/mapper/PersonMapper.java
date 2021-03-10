package com.example.demo.mapper;

import com.example.demo.data.PersonDto;
import com.example.demo.model.Person;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PersonMapper {

    public PersonDto toDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .address(person.getAddress())
                .aliases(person.getAliases())
                .build();
    }

    public Person toModel(PersonDto personDto) {
        String personId = Optional
                .ofNullable(personDto.getId())
                .orElseGet(UUID.randomUUID()::toString);

        return Person.builder()
                .id(personId)
                .firstName(personDto.getFirstName())
                .lastName(personDto.getLastName())
                .address(personDto.getAddress())
                .aliases(personDto.getAliases())
                .build();
    }
}
