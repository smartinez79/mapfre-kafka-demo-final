package com.example.demo.service;

import com.example.demo.data.PersonDto;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.model.Person;
import com.example.demo.repository.MongoPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final MongoPersonRepository personRepository;
    private final PersonMapper personMapper;

    @Autowired
    public PersonService(MongoPersonRepository personRepository,
                         PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public List<PersonDto> findAll() {
        List<Person> models = personRepository.findAll();
        return models
                .stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public PersonDto findById(String id) {
        Optional<Person> model = personRepository.findById(id);
        return model
                .map(personMapper::toDto)
                .orElse(null);
    }

    public List<PersonDto> findByFirstName(String firstName) {
        List<Person> models = personRepository.findByFirstName(firstName);
        return models.stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PersonDto> findByCity(String city) {
        List<Person> models = personRepository.findByAddress_City(city);
        return models.stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PersonDto> findByFirstNameAndCity(String firstName, String city) {
        List<Person> models = personRepository.findByFirstNameAndAddress_City(firstName, city);
        return models.stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public PersonDto createPerson(PersonDto personDto) {
        Person model = personMapper.toModel(personDto);
        Person savedModel = personRepository.save(model);
        return personMapper.toDto(savedModel);
    }

    public PersonDto updatePerson(PersonDto personDto) {
        Optional<Person> model = personRepository.findById(personDto.getId());
        return model
                .map(p -> {
                    p.setFirstName(personDto.getFirstName());
                    p.setLastName(personDto.getLastName());
                    p.setAddress(personDto.getAddress());
                    p.setAliases(personDto.getAliases());
                    return p;
                })
                .map(personRepository::save)
                .map(personMapper::toDto)
                .orElse(null);
    }

    public boolean deletePersonById(String id) {
        Optional<Person> model = personRepository.findById(id);
        if (!model.isPresent()) {
            return false;
        }
        boolean deleted;
        try {
            personRepository.deleteById(id);
            deleted = true;
        } catch (Exception ex) {
            deleted = false;
        }
        return deleted;
    }

    public void deleteAllPeople() {
        personRepository.deleteAll();
    }

    public PersonDto updatePersonAliases(String id, List<String> aliases) {
        Person updatedPerson = personRepository.updateAliases(id, aliases);
        return Optional.ofNullable(updatedPerson)
                .map(personMapper::toDto)
                .orElse(null);
    }
}

