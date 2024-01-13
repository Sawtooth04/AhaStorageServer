package com.sawtooth.ahastorageserver.configurations;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.reactive.function.client.*;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {
    public static final int TIMEOUT = 10000;

    @Bean
    public WebClient webClientWithTimeout(CsrfToken csrfToken) {
        HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT);
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(1048576))
            .build();

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(client))
                .filter((request, next) -> {
                    return next.exchange(request).flatMap(response -> {
                        if (response.statusCode().is4xxClientError())
                    });
                })
            .exchangeStrategies(strategies)
            .build();
    }
}
