package com.sawtooth.ahastorageserver.controllers;

import com.sawtooth.ahastorageserver.models.systeminfo.SystemInfo;
import com.sawtooth.ahastorageserver.services.systemmanager.ISystemManager;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/system")
public class SystemController {
    private final ISystemManager systemManager;

    @Autowired
    public SystemController(ISystemManager systemManager) {
        this.systemManager = systemManager;
    }

    @GetMapping("/info/get")
    @Async
    public CompletableFuture<ResponseEntity<SystemInfo>> GetInfo() {
        SystemInfo result = systemManager.GetSystemInfo();

        result.add(linkTo(methodOn(SystemController.class).GetInfo()).withSelfRel());
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
    }
}
