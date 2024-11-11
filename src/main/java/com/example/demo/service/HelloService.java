package com.example.demo.service;

import com.example.demo.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HelloService {

    private final List<Person> people = new ArrayList<>();

    public HelloService() {
        // 初始化一些假数据
        people.add(new Person(1L, "Alice", 25));
        people.add(new Person(2L, "Bob", 30));
        people.add(new Person(3L, "Charlie", 35));
    }

    public Person getPerson(Long id) {
        return people.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Person savePerson(Person person) {
        people.add(person);
        return person;
    }
}