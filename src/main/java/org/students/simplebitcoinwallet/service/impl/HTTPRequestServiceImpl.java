package org.students.simplebitcoinwallet.service.impl;

import org.students.simplebitcoinwallet.exception.ExternalNodeInvalidHTTPCodeException;
import org.students.simplebitcoinwallet.service.HTTPRequestService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequestServiceImpl implements HTTPRequestService {
    private final String url;

    public HTTPRequestServiceImpl(String  url) {
        this.url = url;
    }

    private HttpURLConnection openConnection(URL url, String method) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        return con;
    }

    private String urlParametersToString(Map<String, String> params) throws IOException {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }
        String resultAsString = result.toString();
        return !resultAsString.isEmpty()
                ?  "?" + resultAsString.substring(0,resultAsString.length()-1)
                : resultAsString;
    }

    private void initializeHeaders(HttpURLConnection con, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            con.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private byte[] readResponseBody(HttpURLConnection con) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(con.getInputStream())){
            return in.readAllBytes();
        }
    }

    private void sendRequestBody(HttpURLConnection con, byte[] requestBody) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(con.getOutputStream())) {
            bufferedOutputStream.write(requestBody);
            bufferedOutputStream.flush();
        }
    }


    @Override
    public byte[] get(String uri, Map<String, String> headers, Map<String, String> urlParams) throws IOException, ExternalNodeInvalidHTTPCodeException {
        URL url = new URL(this.url + uri + urlParametersToString(urlParams));
        HttpURLConnection con = openConnection(url, "GET");
        initializeHeaders(con, headers);

        int status = con.getResponseCode();
        if ((status < 200 || status > 299) && status != 400)
            throw new ExternalNodeInvalidHTTPCodeException("Invalid HTTP status code " + status, status);
        return readResponseBody(con);
    }

    @Override
    public byte[] post(String uri, Map<String, String> headers, byte[] requestBody) throws IOException, ExternalNodeInvalidHTTPCodeException {
        URL url = new URL(this.url + uri);
        HttpURLConnection con = openConnection(url, "POST");
        initializeHeaders(con, headers);
        sendRequestBody(con, requestBody);

        int status = con.getResponseCode();
        if ((status < 200 || status > 299) && status != 400)
            throw new ExternalNodeInvalidHTTPCodeException("Invalid HTTP status code " + status, status);
        return readResponseBody(con);
    }

    @Override
    public byte[] put(String uri, Map<String, String> headers, byte[] requestBody) throws IOException, ExternalNodeInvalidHTTPCodeException {
        URL url = new URL(this.url + uri);
        HttpURLConnection con = openConnection(url, "PUT");
        initializeHeaders(con, headers);
        sendRequestBody(con, requestBody);

        int status = con.getResponseCode();
        if ((status < 200 || status > 299) && status != 400)
            throw new ExternalNodeInvalidHTTPCodeException("Invalid HTTP status code " + status, status);
        return readResponseBody(con);
    }

    @Override
    public byte[] delete(String uri, Map<String, String> headers) throws IOException, ExternalNodeInvalidHTTPCodeException {
        URL url = new URL(this.url + uri);
        HttpURLConnection con = openConnection(url, "DELETE");
        initializeHeaders(con, headers);

        int status = con.getResponseCode();
        if ((status < 200 || status > 299) && status != 400)
            throw new ExternalNodeInvalidHTTPCodeException("Invalid HTTP status code " + status, status);
        return readResponseBody(con);
    }

    public static void main(String[] args) throws Exception {
        HTTPRequestService httpRequestService = new HTTPRequestServiceImpl("http://localhost:8080");
        Map<String, String> map = new HashMap<>();
        map.put("param", "val");
        byte[] bytes = httpRequestService.get("/blockchain/time", new HashMap<>(), map);
        String stringResponse = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(stringResponse);
    }
}
