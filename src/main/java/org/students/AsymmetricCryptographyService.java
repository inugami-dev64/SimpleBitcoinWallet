package org.students;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.KeyPair;

public abstract class AsymmetricCryptographyService {
    public abstract byte[] digestObject(Serializable messageObject);
    // - digests the serializable object and returns a byte[] containing the hash
    // (type of hash is implementation specific)
    public abstract KeyPair generateNewKeypair();
    //- generates new cryptographic keypair

    public abstract byte[] signMessage(Serializable messageObject, byte[] privateKey);
    // - digitally sign Serializable messageObject with private key

    private byte[] byteSerializeTransaction(Transaction transaction) throws SerializationException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
        {
            out.writeObject(transaction.getInputs().size());
            for (TransactionOutput transactionOutput : transaction.getInputs()) {
                out.writeObject(transactionOutput.getSignature());
                out.writeObject(transactionOutput.getAmount());
                out.writeObject(transactionOutput.getReceiverPublicKey());
            }
            out.writeObject(transaction.getOutputs().size());
            for (TransactionOutput transactionOutput : transaction.getOutputs()) {
                out.writeObject(transactionOutput.getAmount());
                out.writeObject(transactionOutput.getReceiverPublicKey());
            }
            out.writeObject(transaction.getSenderPublicKey());
            out.writeObject(transaction.getTimestamp());
            out.flush();
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e) {
            throw new SerializationException("Failed to byte-serialize transaction object: " + e.getMessage());
        }
    }
    // - custom serialization method for Transaction type objects

    private byte[] byteSerializeObject(Serializable serializable) throws SerializationException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
        {
            out.writeObject(serializable);
            out.flush();
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e) {
            throw new SerializationException("Failed to byte-serialize an object: " + e.getMessage());
        }
    }
    // - byte serializes misc Serializable objects.

    protected byte[] byteSerialize(Serializable serializable) throws SerializationException {
        if (serializable instanceof Transaction) {
            return byteSerializeTransaction((Transaction) serializable);
        }
        return byteSerializeObject(serializable);
    }
    // - serialize object according to type specific rules
}
