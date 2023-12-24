package com.sawtooth.ahastorageserver.services.chunksreader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ChunksReader implements IChunksReader {
    @Value("${sys.chunk.folder}")
    private String chunksFolder;

    @Override
    public byte[] Read(String name) throws IOException {
        Path path = Path.of(chunksFolder, name);

        if (Files.exists(path))
            return Files.readAllBytes(path);
        else
            return null;
    }
}
