package com.wbt.sp3reactive.controller;

import com.wbt.sp3reactive.model.Employee;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(path = {"/api/employees"})
public class ApiController {

    public final static Map<String, Employee> DATABASE = new HashMap<>();

    @GetMapping
    public Flux<Employee> employees() {
        return Flux.just(new Employee("leonel", "ka"), new Employee("mansah", "mo"));
    }

    @PostMapping
    public Mono<Employee> add(final @RequestBody Mono<Employee> newEmployee) {
        return newEmployee.map(employee -> {
            DATABASE.put(employee.firstName(), employee);
            return employee;
        });
    }
}
