package com.wbt.sp3reactive.controller;

import com.wbt.sp3reactive.model.Employee;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    public static final Map<String, Employee> DATABASE = new HashMap<>();

    @PostConstruct
    private void initialize() {
        DATABASE.put("nero", new Employee("nero", "ka"));
        DATABASE.put("anna", new Employee("anna", "sm"));
    }

    @GetMapping(path = {"/"})
    public Mono<Rendering> index() {
        return Flux.fromIterable(DATABASE.values())
                .collectList()
                .map(employees -> Rendering
                        .view("index")
                        .modelAttribute("employees", employees)
                        .modelAttribute("newEmployee", new Employee("", ""))
                        .build()
                );
    }

    @PostMapping(path = {"/new-employee"})
    public Mono<String> newEmployee(final @ModelAttribute Mono<Employee> newEmployee) {
        return newEmployee.map(employee -> {
            DATABASE.put(employee.firstName(), employee);
            return "redirect:/";
        });
    }

}
