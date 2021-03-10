package com.example.demo.repository;

import com.example.demo.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoPersonRepository extends MongoRepository<Person, String>, MongoPersonRepositoryCustom {
    List<Person> findByFirstName(String firstName);
    List<Person> findByAddress_City(String city);
    List<Person> findByFirstNameAndAddress_City(String firstName, String city);
}
