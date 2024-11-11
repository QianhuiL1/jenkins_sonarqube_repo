package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id) {
        return helloService.getPerson(id);
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return helloService.savePerson(person);
    }
}