package org.students.simplebitcoinwallet.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionOutput implements Serializable {
    String signature; // - hexadecimal encoded string representing the digital signature of given transaction output.

    BigDecimal amount; // - amount of tokens to transfer

    String receiverPublicKey; // - base58 encoded string representing receiver wallet’s public key

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void setReceiverPublicKey(String receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }
}
