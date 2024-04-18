package org.students.simplebitcoinwallet.data;

import lombok.*;
import org.students.simplebitcoinwallet.exception.InvalidEncodedStringException;
import org.students.simplebitcoinwallet.util.Encoding;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Externalizable {
    private String transactionHash;             // - calculated SHA-256 hash of the transaction
    private List<TransactionOutput> inputs;     // - UTXO’s used as inputs for given transaction
    private List<TransactionOutput> outputs;    // - outputs produced by given transaction
    private String senderPublicKey;             // - base58 encoded string representing sender’s public key
    private LocalDateTime timestamp;            // - timestamp of when the transaction was made

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        try {
            out.writeInt(inputs.size());
            for (TransactionOutput input : inputs) {
                out.write(Encoding.hexStringToBytes(input.getSignature()));
                out.writeObject(input.getAmount());
                out.writeObject(Encoding.defaultPubKeyDecoding(input.getReceiverPublicKey()));
            }
            out.writeInt(outputs.size());
            for (TransactionOutput output : outputs) {
                out.writeObject(output.getAmount());
                out.writeObject(output.getReceiverPublicKey());
            }
            out.write(Encoding.defaultPubKeyDecoding(senderPublicKey));
            out.writeObject(timestamp);
        }
        catch (InvalidEncodedStringException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // read transaction inputs and outputs
        inputs = new ArrayList<>(in.readInt());
        readTransactionOutputs(inputs, in);
        outputs = new ArrayList<>(in.readInt());
        readTransactionOutputs(outputs, in);

        // read sender public key and timestamp
        byte[] senderPublicKey = new byte[88];
        in.read(senderPublicKey);
        this.senderPublicKey = Encoding.defaultPubKeyEncoding(senderPublicKey);
        this.timestamp = (LocalDateTime) in.readObject();
    }

    private void readTransactionOutputs(List<TransactionOutput> transactionOutputs, ObjectInput in) throws IOException, ClassNotFoundException {
        for (int i = 0; i < transactionOutputs.size(); i++) {
            // deserialize input data
            byte[] signature = new byte[72];
            in.read(signature);
            BigDecimal amount = (BigDecimal) in.readObject();
            byte[] receiverPublicKey = new byte[88];
            in.read(receiverPublicKey);

            // save deserialized data into transaction input
            inputs.add(TransactionOutput.builder()
                    .signature(Encoding.toHexString(signature))
                    .amount(amount)
                    .receiverPublicKey(Encoding.defaultPubKeyEncoding(receiverPublicKey))
                    .build()
            );
        }
    }
}
