package com.sawtooth.ahastorageserver.configurations;

import com.sawtooth.ahastorageserver.services.chunkssynchronizer.IChunkSynchronizer;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@EnableAsync
public class AppConfiguration {
    @Value("${cors.allowed-origins}")
    private String[] corsAllowedOrigins;
    @Value("${sys.chunk.folder}")
    private String chunksFolder;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@Nonnull CorsRegistry registry) {
            registry.addMapping("/**")
                .allowedOrigins(corsAllowedOrigins)
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

    @Bean
    public CommandLineRunner CommandLineRunner(IChunkSynchronizer synchronizer) {
        return (args) -> {
            Files.createDirectories(Path.of(chunksFolder));
            if (!synchronizer.Synchronize(corsAllowedOrigins))
                System.exit(126);
        };
    }
}
