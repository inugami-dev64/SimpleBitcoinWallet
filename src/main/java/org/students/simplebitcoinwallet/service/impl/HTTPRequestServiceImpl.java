package org.students.simplebitcoinwallet.service.impl;

import org.students.simplebitcoinwallet.data.HttpRequestMethod;
import org.students.simplebitcoinwallet.service.HTTPRequestService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Map;

public class HTTPRequestServiceImpl implements HTTPRequestService {
    private final String url;

    public HTTPRequestServiceImpl(String  url) {
        this.url = url;
    }

    private HttpRequest buildHttpRequest(URI uri, HttpRequestMethod method, Map<String, String> headers, byte[] requestBody) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri);
        headers.forEach(builder::header);

        // set http method
        switch (method) {
            case GET:
                builder.GET();
                break;

            case POST:
                builder.POST(HttpRequest.BodyPublishers.ofByteArray(requestBody));
                break;

            case PUT:
                builder.PUT(HttpRequest.BodyPublishers.ofByteArray(requestBody));
                break;

            case DELETE:
                builder.DELETE();
                break;
        }

        return builder.build();
    }

    @Override
    public HttpResponse<byte[]> request(String uri, HttpRequestMethod method, Map<String, String> headers, byte[] requestBody) throws IOException {
        try {
            // in Java 17 HttpClient does not implement AutoClosable
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = buildHttpRequest(new URI(this.url + uri), method, headers, requestBody);
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
        }
        catch (InterruptedException | URISyntaxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public HttpResponse<byte[]> request(String uri, HttpRequestMethod method, Map<String, String> headers) throws IOException {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = buildHttpRequest(new URI(this.url + uri), method, headers, new byte[0]);
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
        }
        catch (InterruptedException | URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
