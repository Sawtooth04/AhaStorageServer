package com.sawtooth.ahastorageserver.services.chunksloader;

import com.sawtooth.ahastorageserver.models.chunk.ChunkUploadModel;

import java.io.IOException;

public interface IChunksLoader {
    public void PutChunk(ChunkUploadModel uploadModel) throws IOException;
}
