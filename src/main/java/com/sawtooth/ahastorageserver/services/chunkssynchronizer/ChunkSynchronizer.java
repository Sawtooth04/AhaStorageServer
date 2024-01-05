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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ChunkSynchronizer implements IChunkSynchronizer {
    @Value("${sys.chunk.folder}")
    private String chunksFolder;
    @Value("${server.port}")
    private int port;
    @Value("${sys.chunk.sync.max-threads-count}")
    private int syncThreadsCount;
    private final WebClient webClient;
    private Boolean result;
    private final Object lock = new Object();

    @Autowired
    public ChunkSynchronizer(WebClient webClient) {
        this.webClient = webClient;
    }

    private void SynchronizeWithServer(ChunkSynchronizationModel model, String server) {
        try {
            RepresentationModel<?> links = webClient.get().uri(String.join("", server, "/api/"))
                .retrieve().bodyToMono(RepresentationModel.class).block();

            if (links != null && links.getLink("chunk-sync").isPresent()) {
                webClient.post().uri(String.join("", server, links.getLink("chunk-sync").get()
                    .getHref())).bodyValue(model).retrieve()
                    .bodyToMono(RepresentationModel.class).block();
            }
            else
                synchronized (lock) { result = false; }
        }
        catch (Exception exception) {
            synchronized (lock) { result = false; }
        }
    }

    @Override
    public boolean Synchronize(String[] servers) throws InterruptedException {
        File[] chunks = (new File(chunksFolder)).listFiles();
        ExecutorService threadPool = Executors.newFixedThreadPool(syncThreadsCount);

        result = true;
        if (chunks != null) {
            for (File chunk : chunks)
                for (String server : servers)
                    threadPool.submit(() -> {
                        try {
                            SynchronizeWithServer(new ChunkSynchronizationModel(chunk.getName(),
                                Files.readAllBytes(chunk.toPath()), chunk.lastModified(), port), server);
                        }
                        catch (Exception exception) {
                            synchronized (lock) { result = false; }
                        }
                    });
        }
        else
            return false;
        threadPool.shutdown();
        if (!threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS))
            return false;
        return result;
    }
}
