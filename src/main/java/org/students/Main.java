package org.students;

import java.security.Security;

public class Main {
    public static void main(String[] args) {
        Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
    }
}