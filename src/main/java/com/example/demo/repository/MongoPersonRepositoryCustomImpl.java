package com.example.demo.repository;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoPersonRepositoryCustomImpl implements MongoPersonRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoPersonRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Person updateAliases(String id, List<String> aliases) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        // Check if there exists a person with the given id
        if (!mongoTemplate.exists(query, Person.class)) {
            return null;
        }

        // Update the field "aliases" for the person with the id
        Update update = new Update();
        update.set("aliases", aliases);
        mongoTemplate.updateFirst(query, update, Person.class);

        // Finally, find the updated person and return it
        return mongoTemplate.findOne(query, Person.class);
    }
}