package org.students.simplebitcoinwallet.data;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOutput {
    String signature;           // - hexadecimal encoded string representing the digital signature of given transaction output.
    BigDecimal amount;          // - amount of tokens to transfer
    String receiverPublicKey;   // - base58 encoded string representing receiver wallet’s public key
}
