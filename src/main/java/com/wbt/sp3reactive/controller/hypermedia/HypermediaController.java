package com.wbt.sp3reactive.controller.hypermedia;

import com.wbt.sp3reactive.controller.HomeController;
import com.wbt.sp3reactive.model.Employee;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class HypermediaController {

    @GetMapping(path = {"/hypermedia/employees/{key}"})
    Mono<EntityModel<Employee>> employee(final @PathVariable String key) {
        Mono<Link> selfLink = linkTo(methodOn(HypermediaController.class).employee(key)).withSelfRel().toMono();

        Mono<Link> aggregateRoot = linkTo(methodOn(HypermediaController.class).employees())
                .withRel(LinkRelation.of("employees")).toMono();

        Mono<Tuple2<Link, Link>> links = Mono.zip(selfLink, aggregateRoot);

        return links.map(objects -> EntityModel.of(HomeController.DATABASE.get(key), objects.getT1(), objects.getT2()));
    }

    @GetMapping(path = {"/hypermedia/employees"})
    Mono<CollectionModel<EntityModel<Employee>>> employees() {
        Mono<Link> selfLink = linkTo(methodOn(HypermediaController.class).employees()).withSelfRel().toMono();

        return selfLink
                .flatMap(self -> Flux.fromIterable(HomeController.DATABASE.keySet())
                        .flatMap(this::employee)
                        .collectList()
                        .map(entityModels -> CollectionModel.of(entityModels, self)));
    }


}
