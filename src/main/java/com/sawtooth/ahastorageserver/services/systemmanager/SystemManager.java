package com.sawtooth.ahastorageserver.services.systemmanager;

import com.sawtooth.ahastorageserver.models.chunk.ChunkUploadModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

@Service
public class SystemManager implements ISystemManager{
    @Value("${sys.chunk.folder}")
    private String chunksFolder;
    @Value("${sys.chunk.folder.max-size}")
    private long chunksFolderMaxSize;

    @Override
    public long GetChunksFolderSize() {
        long size = 0;
        File root = new File(chunksFolder);
        Queue<File> files = new ArrayDeque<>(List.of(Objects.requireNonNull(root.listFiles())));

        for (File file : files) {
            if (file.isFile())
                size += file.length();
            else
                files.addAll(List.of(Objects.requireNonNull(file.listFiles())));
        }
        return size;
    }

    @Override
    public boolean IsAbleToUploadChunk(ChunkUploadModel uploadModel) {
        return uploadModel.data().length + GetChunksFolderSize() <= chunksFolderMaxSize;
    }
}
