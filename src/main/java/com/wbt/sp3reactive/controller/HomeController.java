package com.wbt.sp3reactive.controller;

import com.wbt.sp3reactive.model.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
public class HomeController {

    private final Map<String, Employee> DATABASE = Map.of(
            "nero", new Employee("nero", "ka"),
            "anna", new Employee("anna", "sm")
    );

    @GetMapping(path = {"/"})
    public Mono<Rendering> index() {
        return Flux.fromIterable(DATABASE.values())
                .collectList()
                .map(employees -> Rendering
                        .view("index")
                        .modelAttribute("employees", employees)
                        .build()
                );
    }

}
