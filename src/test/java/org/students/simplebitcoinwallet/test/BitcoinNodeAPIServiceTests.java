package org.students.simplebitcoinwallet.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.students.simplebitcoinwallet.data.Transaction;
import org.students.simplebitcoinwallet.data.TransactionOutput;
import org.students.simplebitcoinwallet.data.TransactionQueryType;
import org.students.simplebitcoinwallet.exception.ExternalNodeInvalidHTTPCodeException;
import org.students.simplebitcoinwallet.service.BitcoinNodeAPIService;
import org.students.simplebitcoinwallet.service.HTTPRequestService;
import org.students.simplebitcoinwallet.service.impl.BitcoinNodeAPIServiceImpl;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
public class BitcoinNodeAPIServiceTests {
    @Mock
    private HTTPRequestService httpRequestService;
    @Mock
    private HttpResponse<byte[]> httpResponse;
    @Mock
    private KeyPair testKeyPair;
    @Mock
    private PublicKey testPublicKey;


    private String getSampleTransactionJson() {
        return """
                [{
                    "transactionHash": "11223344556677889900",
                    "inputs": [
                        {
                            "signature": "InvalidSignature",
                            "amount": 1000.0,
                            "receiverPublicKey": "SatoshiNakamoto"
                        }
                    ],
                    "outputs": [
                        {
                            "signature": "BadOutputSignature1",
                            "amount": 500.0,
                            "receiverPublicKey": "JohnDoe"
                        },
                        {
                            "signature": "BadOutputSignature2",
                            "amount": 500.0,
                            "receiverPublicKey": "SatoshiNakamoto"
                        }
                    ],
                    "senderPublicKey": "SatoshiNakamoto",
                    "timestamp": "2006-01-01T00:00:00"
                }]
                """;
    }
    private Transaction getSampleTransactionObject() {
        return Transaction.builder()
                .transactionHash("11223344556677889900")
                .inputs(List.of(
                        TransactionOutput.builder()
                                .signature("InvalidSignature")
                                .amount(BigDecimal.valueOf(1000.0))
                                .receiverPublicKey("SatoshiNakamoto")
                                .build()

                ))
                .outputs(List.of(
                        TransactionOutput.builder()
                                .signature("BadOutputSignature1")
                                .amount(BigDecimal.valueOf(500.0))
                                .receiverPublicKey("JohnDoe")
                                .build(),
                        TransactionOutput.builder()
                                .signature("BadOutputSignature2")
                                .amount(BigDecimal.valueOf(500.0))
                                .receiverPublicKey("SatoshiNakamoto")
                                .build()
                ))
                .senderPublicKey("SatoshiNakamoto")
                .timestamp(LocalDateTime.parse("2006-01-01T00:00:00"))
                .build();
    }

    @Test
    @DisplayName("Ensure that getTransactions work and return expected output")
    void testGetTransactions_ExpectValidTransaction() throws Exception {
        given(httpResponse.body()).willReturn(getSampleTransactionJson().getBytes(StandardCharsets.UTF_8));
        given(httpResponse.statusCode()).willReturn(200);
        given(httpRequestService.request(any(),any(), any()))
                .willReturn(httpResponse);
        given(testPublicKey.getEncoded()).willReturn(new byte[178]);
        given(testKeyPair.getPublic()).willReturn(testPublicKey);

        BitcoinNodeAPIService bitcoinNodeAPIService = new BitcoinNodeAPIServiceImpl(httpRequestService);

        List<Transaction> transactions = bitcoinNodeAPIService.getTransactions(TransactionQueryType.ALL, testKeyPair.getPublic());
        Assertions.assertEquals(1, transactions.size());

        Transaction expected = getSampleTransactionObject();
        Transaction actual = transactions.get(0);

        Assertions.assertEquals(expected.getTransactionHash(), actual.getTransactionHash());
        Assertions.assertEquals(expected.getInputs().size(), actual.getInputs().size());
        for (int i = 0; i < expected.getInputs().size(); i++) {
            Assertions.assertEquals(expected.getInputs().get(i).getSignature(), actual.getInputs().get(i).getSignature());
            Assertions.assertEquals(expected.getInputs().get(i).getAmount(), actual.getInputs().get(i).getAmount());
            Assertions.assertEquals(expected.getInputs().get(i).getReceiverPublicKey(), actual.getInputs().get(i).getReceiverPublicKey());
        }
        Assertions.assertEquals(expected.getOutputs().size(), actual.getOutputs().size());
        for (int i = 0; i < expected.getOutputs().size(); i++) {
            Assertions.assertEquals(expected.getOutputs().get(i).getSignature(), actual.getOutputs().get(i).getSignature());
            Assertions.assertEquals(expected.getOutputs().get(i).getAmount(), actual.getOutputs().get(i).getAmount());
            Assertions.assertEquals(expected.getOutputs().get(i).getReceiverPublicKey(), actual.getOutputs().get(i).getReceiverPublicKey());
        }
        Assertions.assertEquals(expected.getSenderPublicKey(), actual.getSenderPublicKey());
        Assertions.assertEquals(expected.getTimestamp(), actual.getTimestamp());

    }
    @Test
    @DisplayName("Ensure that exception is thrown if response contains invalid status code")
    void testGetTransactions_InvalidStatusCode_ExceptThrows() throws Exception {
        given(httpResponse.statusCode()).willReturn(400);
        given(httpRequestService.request(any(),any(), any()))
                .willReturn(httpResponse);
        given(testPublicKey.getEncoded()).willReturn(new byte[178]);
        given(testKeyPair.getPublic()).willReturn(testPublicKey);

        BitcoinNodeAPIService bitcoinNodeAPIService = new BitcoinNodeAPIServiceImpl(httpRequestService);

        Assertions.assertThrows(ExternalNodeInvalidHTTPCodeException.class, () -> bitcoinNodeAPIService.getTransactions(TransactionQueryType.ALL, testKeyPair.getPublic()));
    }

    @Test
    @DisplayName("Ensure that server uptime is correctly parsed")
    void testStatus_ExpectTrue() throws Exception {
        String statusJson = """
                {
                  "status": "UP",
                  "components": {
                    "db": {
                      "status": "UP",
                      "details": {
                        "database": "H2",
                        "validationQuery": "isValid()"
                      }
                    },
                    "ping": {
                      "status": "UP"
                    }
                  }
                }
                """;
        given(httpResponse.body()).willReturn(statusJson.getBytes(StandardCharsets.UTF_8));
        given(httpResponse.statusCode()).willReturn(200);
        given(httpRequestService.request(any(),any(), any()))
                .willReturn(httpResponse);

        BitcoinNodeAPIService bitcoinNodeAPIService = new BitcoinNodeAPIServiceImpl(httpRequestService);

        Assertions.assertTrue(bitcoinNodeAPIService.status());
    }
    @Test
    @DisplayName("Ensure that server uptime is considered down if status code is invalid")
    void testStatus_InvalidStatusCode_ExpectFalse() throws Exception {
        given(httpResponse.statusCode()).willReturn(400);
        given(httpRequestService.request(any(),any(), any()))
                .willReturn(httpResponse);

        BitcoinNodeAPIService bitcoinNodeAPIService = new BitcoinNodeAPIServiceImpl(httpRequestService);

        Assertions.assertFalse(bitcoinNodeAPIService.status());
    }

    @Test
    @DisplayName("Ensure that server time gets returned correctly")
    void testServerTime_ExpectEquals() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        given(httpResponse.body()).willReturn(time.toString().getBytes(StandardCharsets.UTF_8));
        given(httpResponse.statusCode()).willReturn(200);
        given(httpRequestService.request(any(),any(), any()))
                .willReturn(httpResponse);

        BitcoinNodeAPIService bitcoinNodeAPIService = new BitcoinNodeAPIServiceImpl(httpRequestService);

        Assertions.assertEquals(time, bitcoinNodeAPIService.serverTime());
    }

    @Test
    @DisplayName("Ensure that exception is thrown when response code is invalid")
    void testServerTime_InvalidStatusCode_ExpectThrows() throws Exception {
        given(httpResponse.statusCode()).willReturn(400);
        given(httpRequestService.request(any(),any(), any()))
                .willReturn(httpResponse);

        BitcoinNodeAPIService bitcoinNodeAPIService = new BitcoinNodeAPIServiceImpl(httpRequestService);

        Assertions.assertThrows(ExternalNodeInvalidHTTPCodeException.class, bitcoinNodeAPIService::serverTime);
    }
}
