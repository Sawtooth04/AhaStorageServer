package com.sawtooth.ahastorageserver.models.systeminfo;

import com.sawtooth.ahastorageserver.models.chunk.ChunkDownloadModel;
import org.springframework.hateoas.RepresentationModel;

public class SystemInfo extends RepresentationModel<ChunkDownloadModel> {
    private long allocatedMemory;
    private long usedMemory;

    public SystemInfo() {
        this.allocatedMemory = 0;
        this.usedMemory = 0;
    }

    public SystemInfo(long allocatedMemory, long usedMemory) {
        this.allocatedMemory = allocatedMemory;
        this.usedMemory = usedMemory;
    }

    public long getAllocatedMemory() {
        return allocatedMemory;
    }

    public void setAllocatedMemory(long allocatedMemory) {
        this.allocatedMemory = allocatedMemory;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }
}
