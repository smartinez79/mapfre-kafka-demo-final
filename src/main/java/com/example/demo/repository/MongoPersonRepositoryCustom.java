package com.example.demo.repository;

import com.example.demo.model.Person;

import java.util.List;

public interface MongoPersonRepositoryCustom {
    Person updateAliases(String id, List<String> aliases);
}
