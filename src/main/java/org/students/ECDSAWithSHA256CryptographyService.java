package org.students;

import java.io.Serializable;
import java.security.KeyPair;

public class ECDSAWithSHA256CryptographyService extends AsymmetricCryptographyServiceSHA256{
    @Override
    public KeyPair generateNewKeypair() {
        return null;
    }

    @Override
    public byte[] signMessage(Serializable messageObject, byte[] privateKey) {
        return new byte[0];
    }
}
