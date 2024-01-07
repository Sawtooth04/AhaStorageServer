package com.sawtooth.ahastorageserver.controllers;

import com.sawtooth.ahastorageserver.models.main.MainResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class MainController {
    @GetMapping("/")
    @Async
    public CompletableFuture<ResponseEntity<MainResponse>> Main() {
        MainResponse result = new MainResponse();

        result.add(linkTo(methodOn(MainController.class).Main()).withSelfRel());
        result.add(linkTo(methodOn(SystemController.class).GetInfo()).withRel("system-info"));
        result.add(linkTo(methodOn(ChunkController.class).Get(null)).withRel("chunk-get"));
        result.add(linkTo(methodOn(ChunkController.class).GetExists(null)).withRel("chunk-exists-get"));
        result.add(linkTo(methodOn(ChunkController.class).Put(null)).withRel("chunk-put"));
        result.add(linkTo(methodOn(ChunkController.class).Delete(null)).withRel("chunk-delete"));
        result.add(linkTo(methodOn(ChunkController.class).GetModified(null)).withRel("chunk-modified-get"));
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
    }
}