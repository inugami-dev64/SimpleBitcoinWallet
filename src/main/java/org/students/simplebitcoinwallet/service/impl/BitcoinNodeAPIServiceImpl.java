package org.students.simplebitcoinwallet.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.students.simplebitcoinwallet.data.*;
import org.students.simplebitcoinwallet.exception.ExternalNodeInvalidHTTPCodeException;
import org.students.simplebitcoinwallet.exception.ExternalNodeValidationException;
import org.students.simplebitcoinwallet.service.BitcoinNodeAPIService;
import org.students.simplebitcoinwallet.service.HTTPRequestService;
import org.students.simplebitcoinwallet.util.Encoding;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitcoinNodeAPIServiceImpl implements BitcoinNodeAPIService {
    private final HTTPRequestService httpRequestService;

    public BitcoinNodeAPIServiceImpl(HTTPRequestService httpRequestService) {
        this.httpRequestService = httpRequestService;
    }
    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "SimpleBitcoinWallet v1.0");
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    public List<Transaction> getTransactions(TransactionQueryType queryType, PublicKey publicKey) throws ExternalNodeInvalidHTTPCodeException {
        final String endPointURI = "/blockchain/transactions?pubKey=" +
                Encoding.defaultPubKeyEncoding(publicKey.getEncoded()) +
                "&type=" + queryType.toString();
        try {
            HttpResponse<byte[]> response = httpRequestService.request(endPointURI, HttpRequestMethod.GET, getHeaders());
            if (response.statusCode() > 299)
                throw new ExternalNodeInvalidHTTPCodeException("Status code is larger than 299", response.statusCode());
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<List<Transaction>>() {});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransactionOutput> getUnspentTransactionOutputs(PublicKey publicKey) throws ExternalNodeInvalidHTTPCodeException {
        final String endpointURI = "/blockchain/utxo?pubKey=" + Encoding.defaultPubKeyEncoding(publicKey.getEncoded());
        try {
            HttpResponse<byte[]> response = httpRequestService.request(endpointURI, HttpRequestMethod.GET, getHeaders());
            if (response.statusCode() > 299)
                throw new ExternalNodeInvalidHTTPCodeException("Status code is larger than 299", response.statusCode());
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<List<TransactionOutput>>() {});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean status() {
        final String endPointURI = "/blockchain/status";
        try {
            HttpResponse<byte[]> response = httpRequestService.request(endPointURI, HttpRequestMethod.GET, getHeaders());
            if (response.statusCode() > 299)
                return false;
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> statusResponse = objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
            return statusResponse.containsKey("status") && ((String) statusResponse.get("status")).equalsIgnoreCase("up");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public LocalDateTime serverTime() throws ExternalNodeInvalidHTTPCodeException {
        final String endPointURI = "/blockchain/time";
        try {
            HttpResponse<byte[]> response = httpRequestService.request(endPointURI, HttpRequestMethod.GET, getHeaders());
            if (response.statusCode() > 299)
                throw new ExternalNodeInvalidHTTPCodeException("Status code larger than 299", response.statusCode());
            String time = new String(response.body(), StandardCharsets.UTF_8);
            return LocalDateTime.parse(time);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction brodcastTransaction(Transaction transaction) throws ExternalNodeInvalidHTTPCodeException, ExternalNodeValidationException {
        final String endPointURI = "/blockchain/send";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(transaction);

            HttpResponse<byte[]> response = httpRequestService.request(endPointURI, HttpRequestMethod.POST, getHeaders(), json.getBytes(StandardCharsets.UTF_8));
            if (response.statusCode() > 299 && response.statusCode() != 400)
                throw new ExternalNodeInvalidHTTPCodeException("Status code larger than 299", response.statusCode());

            if (response.statusCode() == 400){
                ValidationErrorResponse validationErrorResponse = objectMapper.readValue(response.body(), ValidationErrorResponse.class);
                throw new ExternalNodeValidationException("Transaction validation error has occurred", validationErrorResponse);
            }
            return objectMapper.readValue(response.body(), Transaction.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
