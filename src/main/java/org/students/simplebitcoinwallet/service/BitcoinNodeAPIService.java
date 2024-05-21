package org.students.simplebitcoinwallet.service;

import org.students.simplebitcoinwallet.data.Transaction;
import org.students.simplebitcoinwallet.data.TransactionOutput;
import org.students.simplebitcoinwallet.data.TransactionQueryType;
import org.students.simplebitcoinwallet.exception.ExternalNodeInvalidHTTPCodeException;
import org.students.simplebitcoinwallet.exception.ExternalNodeValidationException;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;

/**
 * rest API abstraction interface
 */
public interface BitcoinNodeAPIService {
    /**
     * Queries transactions from the server where given wallet has been a participant in
     * @param queryType type of transactions to query for
     * @param publicKey wallet public key to use in the query
     * @return list of transactions where given wallet has participated in
     */
    List<Transaction> getTransactions(TransactionQueryType queryType, PublicKey publicKey) throws ExternalNodeInvalidHTTPCodeException;

    /**
     * Queries unspent transaction outputs for given wallet from the node server
     * @param publicKey wallet public key to use in the query
     * @return list of unspent transaction outputs for given wallet
     */
    List<TransactionOutput> getUnspentTransactionOutputs(PublicKey publicKey) throws ExternalNodeInvalidHTTPCodeException;

    /**
     * Checks if given server is available
     * @return true when server is available and false otherwise
     */
    boolean status();

    /**
     * Asks UTC time from server
     * @return UTC server time
     */
    LocalDateTime serverTime() throws ExternalNodeInvalidHTTPCodeException;

    /**
     * Broadcasts transaction to known node server
     * @param transaction transaction to broadcast
     * @return same transaction object specified in the arguments if the transaction was successfully broadcast.
     */
    Transaction brodcastTransaction(Transaction transaction) throws ExternalNodeInvalidHTTPCodeException, ExternalNodeValidationException;
}
