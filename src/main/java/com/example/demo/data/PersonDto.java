package com.example.demo.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonDto {
    private String id;
    private String firstName;
    private String lastName;
    private Address address;
    private List<String> aliases;
}
