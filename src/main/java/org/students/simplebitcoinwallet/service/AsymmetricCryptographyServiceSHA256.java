package org.students.simplebitcoinwallet.service;

import org.students.simplebitcoinwallet.exception.SerializationException;

import java.io.Serializable;
import java.security.*;
import java.security.NoSuchAlgorithmException;
public abstract class AsymmetricCryptographyServiceSHA256 extends AsymmetricCryptographyService {
    protected AsymmetricCryptographyServiceSHA256(String className) {
        super(className);
    }

    /**
     * Digests given messageObject and returns its calculated SHA-256 hash.
     * @param messageObject specifies the Serializable object to use as a message for hashing
     * @return byte array containing the calculated hash
     */
    @Override
    public byte[] digestObject(Serializable messageObject) throws SerializationException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(byteSerialize(messageObject));
        }
        catch (NoSuchAlgorithmException e) {
            getLogger().severe("MessageDigest implementation does not support SHA-256 hashing: " + e.getMessage());
            return new byte[0];
        }
    }
}
