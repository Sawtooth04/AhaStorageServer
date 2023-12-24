package com.sawtooth.ahastorageserver.services.chunksreader;

import java.io.IOException;

public interface IChunksReader {
    public byte[] Read(String name) throws IOException;
}
