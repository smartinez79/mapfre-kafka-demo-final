package com.example.demo.model;

import com.example.demo.data.Address;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "people")
public class Person {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Address address;
    private List<String> aliases;
}
