package com.sawtooth.ahastorageserver.services.systemmanager;

import com.sawtooth.ahastorageserver.models.chunk.ChunkUploadModel;
import com.sawtooth.ahastorageserver.models.systeminfo.SystemInfo;

public interface ISystemManager {
    public long GetChunksFolderSize();

    public boolean IsAbleToUploadChunk(ChunkUploadModel uploadModel);

    public SystemInfo GetSystemInfo();
}
