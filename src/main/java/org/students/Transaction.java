package org.students;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Transaction implements Serializable {
    String transactionHash; //- calculated SHA-256 hash of the transaction

    List<TransactionOutput> inputs; // - UTXO’s used as inputs for given transaction

    List<TransactionOutput> outputs; // - outputs produced by given transaction

    String senderPublicKey; // - base58 encoded string representing sender’s public key

    LocalDateTime timestamp; // - timestamp of when the transaction was made

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public List<TransactionOutput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionOutput> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
