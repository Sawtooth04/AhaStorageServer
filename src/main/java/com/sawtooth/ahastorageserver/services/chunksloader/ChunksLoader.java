package com.sawtooth.ahastorageserver.services.chunksloader;

import com.sawtooth.ahastorageserver.models.chunk.ChunkUploadModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ChunksLoader implements IChunksLoader {
    @Value("${sys.chunk.folder}")
    private String chunksFolder;

    @Override
    public void PutChunk(ChunkUploadModel uploadModel) throws IOException {
        Path root = Path.of(chunksFolder), file = Path.of(chunksFolder, uploadModel.name());

        Files.createDirectories(root);
        Files.write(file, uploadModel.data());
    }
}
