package com.sawtooth.ahastorageserver.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.sawtooth.ahastorageserver.models.chunk.ChunkDownloadModel;
import com.sawtooth.ahastorageserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahastorageserver.services.chunksloader.IChunksLoader;
import com.sawtooth.ahastorageserver.services.chunksreader.IChunksReader;
import com.sawtooth.ahastorageserver.services.systemmanager.ISystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/chunk")
public class ChunkController {
    private final IChunksLoader chunksLoader;
    private final IChunksReader chunksReader;
    private final ISystemManager systemManager;

    @Autowired
    public ChunkController(IChunksLoader chunksLoader, IChunksReader chunksReader, ISystemManager systemManager) {
        this.chunksLoader = chunksLoader;
        this.chunksReader = chunksReader;
        this.systemManager = systemManager;
    }

    @GetMapping("/get")
    public ResponseEntity<ChunkDownloadModel> Get(@RequestParam String name) {
        try {
            byte[] chunk = chunksReader.Read(name);
            ChunkDownloadModel result = new ChunkDownloadModel(chunk);

            result.add(linkTo(methodOn(ChunkController.class).Get(name)).withSelfRel());
            if (chunk != null)
                return ResponseEntity.status(HttpStatus.OK).body(result);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/put")
    public ResponseEntity<RepresentationModel<?>> Put(@RequestBody ChunkUploadModel uploadModel) {
        RepresentationModel<?> result = new RepresentationModel<>();

        try {
            if (systemManager.IsAbleToUploadChunk(uploadModel)) {
                chunksLoader.PutChunk(uploadModel);
                result.add(linkTo(methodOn(ChunkController.class).Get(uploadModel.name())).withSelfRel());
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
        catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
    }
}
