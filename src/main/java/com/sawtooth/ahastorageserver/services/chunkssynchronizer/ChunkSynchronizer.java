package com.sawtooth.ahastorageserver.services.chunkssynchronizer;

import com.sawtooth.ahastorageserver.models.chunk.ChunkSynchronizationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class ChunkSynchronizer implements IChunkSynchronizer {
    @Value("${sys.chunk.folder}")
    private String chunksFolder;
    @Value("${server.port}")
    private int port;
    private final WebClient webClient;

    @Autowired
    public ChunkSynchronizer(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean SynchronizeWithServer(ChunkSynchronizationModel model, String server) {
        try {
            RepresentationModel<?> links = webClient.get().uri(String.join("", server, "/api/"))
                .retrieve().bodyToMono(RepresentationModel.class).block();

            if (links != null && links.getLink("chunk-sync").isPresent()) {
                webClient.post().uri(String.join("", server, links.getLink("chunk-sync").get()
                    .getHref())).bodyValue(model).retrieve()
                    .bodyToMono(RepresentationModel.class).block();
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }

    @Override
    public boolean Synchronize(String[] servers) throws IOException {
        File[] chunks = (new File(chunksFolder)).listFiles();

        if (chunks != null) {
            for (File chunk : chunks)
                for (String server : servers)
                    if (!SynchronizeWithServer(new ChunkSynchronizationModel(chunk.getName(), Files.readAllBytes(chunk.toPath()),
                        chunk.lastModified(), port), server))
                        return false;
        }
        else
            return false;
        return true;
    }
}
