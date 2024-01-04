package com.sawtooth.ahastorageserver.models.chunk;

public record ChunkSynchronizationModel(String name, byte[] data, long lastModified, int port) {
}
