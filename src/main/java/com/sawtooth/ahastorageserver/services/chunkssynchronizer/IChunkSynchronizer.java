package com.sawtooth.ahastorageserver.services.chunkssynchronizer;

import java.io.IOException;

public interface IChunkSynchronizer {
    public boolean Synchronize(String[] servers) throws IOException, InterruptedException;
}
