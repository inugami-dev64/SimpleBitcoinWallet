package org.students;

import java.io.Serializable;

public abstract class AsymmetricCryptographyServiceSHA256 extends AsymmetricCryptographyService{
    @Override
    public byte[] digestObject(Serializable messageObject) {
        return new byte[0];
    }
}
