package org.students;

import java.io.Serializable;
import java.security.*;
import java.security.NoSuchAlgorithmException;
public abstract class AsymmetricCryptographyServiceSHA256 extends AsymmetricCryptographyService{
    protected AsymmetricCryptographyServiceSHA256(String className) {
        super(className);
    }
    @Override
    public byte[] digestObject(Serializable messageObject) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(byteSerialize(messageObject));
        }
        catch (NoSuchAlgorithmException e) {
            getLogger().severe("MessageDigest implementation does not support SHA-256 hashing: " + e.getMessage());
            return new byte[0];
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }
}
