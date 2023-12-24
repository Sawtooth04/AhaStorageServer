package com.sawtooth.ahastorageserver.services.systemmanager;

import com.sawtooth.ahastorageserver.models.chunk.ChunkUploadModel;

public interface ISystemManager {
    public long GetChunksFolderSize();

    public boolean IsAbleToUploadChunk(ChunkUploadModel uploadModel);
}
