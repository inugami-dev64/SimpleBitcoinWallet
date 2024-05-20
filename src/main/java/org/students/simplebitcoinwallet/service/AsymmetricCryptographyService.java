package org.students.simplebitcoinwallet.service;

import org.students.simplebitcoinwallet.exception.MalformedKeyException;
import org.students.simplebitcoinwallet.exception.MalformedSignatureException;
import org.students.simplebitcoinwallet.exception.SerializationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.logging.Logger;

public abstract class AsymmetricCryptographyService {
    private final Logger logger;

    protected AsymmetricCryptographyService(String className) {
        logger = Logger.getLogger(className);
    }

    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Digests given messageObject and returns its calculated hash. The specific hashing algorithm is implementation dependent
     * @param messageObject specifies the object to use as a message for hashing
     * @return byte array containing the calculated hash
     */
    public abstract byte[] digestObject(Serializable messageObject) throws SerializationException;

    /**
     * Verifies if the digital signature matches signer's public key and the message that was signed.
     * @param messageObject specifies the Serializable object that composes the message
     * @param signature specifies the signature itself as a byte array
     * @param pubKey specifies the signer's public key
     * @return a boolean value with either true, if signature verification was successful, or false otherwise
     */
    public abstract boolean verifyDigitalSignature(Serializable messageObject, byte[] signature, byte[] pubKey) throws SerializationException, MalformedKeyException, MalformedSignatureException;

    /**
     * Generates a new public/private keypair
     * @return a KeyPair object representing public/private keys
     */
    public abstract KeyPair generateNewKeypair();

    /**
     * Signs given byte array composed of serialized messageObject with provided private key
     * @param messageObject specifies the message object to sign
     * @param privateKey specifies an array of bytes encoded in implementation specific manner
     * @return array of bytes containing the digital signature
     */
    public abstract byte[] signMessage(Serializable messageObject, byte[] privateKey) throws SerializationException, MalformedKeyException;

    /**
     * Serialize given object into byte array
     * @param serializable specifies the object to serialize
     * @return serialized object's byte array
     */
    protected byte[] byteSerialize(Serializable serializable) throws SerializationException {
        if (serializable instanceof String)
            return ((String) (serializable)).getBytes(StandardCharsets.UTF_8);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
        {
            out.reset();
            byteArrayOutputStream.reset();
            if (serializable instanceof Externalizable)
                ((Externalizable)serializable).writeExternal(out);
            else out.writeObject(serializable);
            out.flush();
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e) {
            throw new SerializationException("Failed to byte-serialize an object: " + e.getMessage());
        }
    }
}
