package com.sawtooth.ahastorageserver.services.csrftokenstorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.util.Objects;

@Service
public class CsrfTokenStorage implements ICsrfTokenStorage {
    @Value("${sys.csrf.token.name}")
    private String tokenCookieName;
    @Value("${sys.csrf.token.header.name}")
    private String tokenCookieHeaderName;
    private static String token;

    static {
        token = "";
    }

    public ClientRequest.Builder Consume(ClientRequest.Builder builder) {
        builder.cookie(tokenCookieName, token);
        builder.header(tokenCookieHeaderName, token);
        return builder;
    }

    public void Set(MultiValueMap<String, ResponseCookie> map) {
        if (map.containsKey(tokenCookieName))
            token = Objects.requireNonNull(map.getFirst(tokenCookieName)).getValue();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", tokenCookieName, token);
    }
}
