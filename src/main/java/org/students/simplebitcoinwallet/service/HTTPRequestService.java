package org.students.simplebitcoinwallet.service;

import org.students.simplebitcoinwallet.data.HttpRequestMethod;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Service interface for making generic http type requests
 */
public interface HTTPRequestService {
    /**
     * Performs an HTTP request for specified URL
     * @param uri relative URL path
     * @param method specifies the HTTP request method to use
     * @param headers HTTP headers
     * @param requestBody specifies the request body to use
     * @return byte array containing the response body
     */
     HttpResponse<byte[]> request(String uri, HttpRequestMethod method, Map<String, String> headers, byte[] requestBody) throws IOException;

    /**
     * Performs an HTTP request for specified URL
     * @param uri relative URL path
     * @param method specifies the HTTP request method to use
     * @param headers HTTP headers
     * @return byte array containing the response body
     */
    HttpResponse<byte[]> request(String uri, HttpRequestMethod method, Map<String, String> headers) throws IOException;
}
