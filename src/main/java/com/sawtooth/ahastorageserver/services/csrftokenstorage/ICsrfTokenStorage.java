package com.sawtooth.ahastorageserver.services.csrftokenstorage;

import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;

public interface ICsrfTokenStorage {
    public ClientRequest.Builder Consume(ClientRequest.Builder builder);

    public void Set(MultiValueMap<String, ResponseCookie> map);
}
