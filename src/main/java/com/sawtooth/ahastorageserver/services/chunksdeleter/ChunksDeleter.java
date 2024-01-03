package com.sawtooth.ahastorageserver.services.chunksdeleter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ChunksDeleter implements IChunksDeleter {
    @Value("${sys.chunk.folder}")
    private String chunksFolder;

    @Override
    public boolean Delete(String name) {
        return (new File(String.join("/", chunksFolder, name))).delete();
    }
}
