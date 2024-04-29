package org.students.simplebitcoinwallet.service;

import org.students.simplebitcoinwallet.exception.ExternalNodeInvalidHTTPCodeException;

import java.io.IOException;
import java.util.Map;

/**
 * Service interface for making generic http type requests
 */
public interface HTTPRequestService {
    /**
     * Performs an HTTP GET request for specified URI
     * @param uri relative URL path
     * @param headers HTTP headers
     * @param urlParams URL parameters
     * @return byte array containing the response body
     */
    byte[] get(String uri, Map<String, String> headers, Map<String, String> urlParams) throws IOException, ExternalNodeInvalidHTTPCodeException;

    /**
     * Performs an HTTP POST request for specified URL
     * @param uri relative URL path
     * @param headers HTTP headers
     * @param requestBody byte array containing the request body
     * @return byte array containing the response body
     */
    byte[] post(String uri, Map<String,String> headers, byte[] requestBody) throws IOException, ExternalNodeInvalidHTTPCodeException;

    /**
     * Performs an HTTP PUT request for specified URL
     * @param uri relative URL path
     * @param headers HTTP headers
     * @param requestBody byte array containing the request body
     * @return byte array containing the response body
     */
    byte[] put(String uri, Map<String, String> headers, byte[] requestBody) throws IOException, ExternalNodeInvalidHTTPCodeException;

    /**
     * Performs an HTTP DELETE request for specified URL
     * @param uri relative URL path
     * @param headers HTTP headers
     * @return byte array containing the response body
     */
    byte[] delete(String uri, Map<String, String> headers) throws IOException, ExternalNodeInvalidHTTPCodeException;
}
