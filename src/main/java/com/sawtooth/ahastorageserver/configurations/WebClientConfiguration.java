package com.sawtooth.ahastorageserver.configurations;

import com.sawtooth.ahastorageserver.services.csrftokenstorage.ICsrfTokenStorage;
import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {
    private static final int TIMEOUT = 10000;

    @Bean
    public WebClient webClientWithTimeout(ICsrfTokenStorage csrfTokenStorage) {
        HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT);
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(1048576))
            .build();

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(client))
            .filter(CsrfFilter(csrfTokenStorage))
            .exchangeStrategies(strategies)
            .build();
    }

    private ExchangeFilterFunction CsrfFilter(ICsrfTokenStorage csrfTokenStorage) {
        return ((request, next) -> {
            return next.exchange(csrfTokenStorage.Consume(ClientRequest.from(request)).build()).flatMap(response -> {
                csrfTokenStorage.Set(response.cookies());
                if (response.statusCode().is4xxClientError())
                    return next.exchange(csrfTokenStorage.Consume(ClientRequest.from(request)).build()).flatMap(Mono::just);
                return Mono.just(response);
            });
        });
    }
}
