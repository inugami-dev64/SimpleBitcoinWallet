package org.students.simplebitcoinwallet.service.impl;

import org.students.simplebitcoinwallet.exception.CryptoProviderNotFoundException;
import org.students.simplebitcoinwallet.service.MalformedKeyException;
import org.students.simplebitcoinwallet.exception.SerializationException;
import org.students.simplebitcoinwallet.service.AsymmetricCryptographyServiceSHA256;

import java.io.Serializable;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class ECDSAWithSHA256CryptographyService extends AsymmetricCryptographyServiceSHA256 {
    private final ECGenParameterSpec ecSpec;
    private final KeyPairGenerator generator;

    public ECDSAWithSHA256CryptographyService() {
        super(ECDSAWithSHA256CryptographyService.class.getName());
        try {
            ecSpec = new ECGenParameterSpec("secp256k1");
            generator = KeyPairGenerator.getInstance("EC");
        }
        catch (NoSuchAlgorithmException e) {
            throw new CryptoProviderNotFoundException("Current crypto provider does not support elliptic-curve cryptography: " + e.getMessage());
        }
    }

    @Override
    public KeyPair generateNewKeypair() {
        try {
            generator.initialize(ecSpec, new SecureRandom());
            return generator.generateKeyPair();
        }
        catch (InvalidAlgorithmParameterException e) {
            getLogger().severe("Current crypto provider does not support elliptic-curve key generation: " + e.getMessage());
            return null;
        }
    }

    @Override
    public byte[] signMessage(Serializable messageObject, byte[] privateKey) throws MalformedKeyException {
        try {
            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
            KeyFactory kf = KeyFactory.getInstance("EC");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey);
            PrivateKey privKey = kf.generatePrivate(privateKeySpec);
            ecdsaSign.initSign(privKey);
            ecdsaSign.update(byteSerialize(messageObject));
            return ecdsaSign.sign();
        }
        catch (NoSuchAlgorithmException e) {
            getLogger().severe("Current crypto provider does not support creating digital signatures with 'SHA256withECDSA' algorithm");
            throw new CryptoProviderNotFoundException(e.getMessage());
        }
        catch (InvalidKeySpecException | InvalidKeyException e) {
            getLogger().warning("Private key used for attempted signing was malformed");
            throw new MalformedKeyException(e.getMessage());
        }
        catch (SignatureException e) {
            // temporary fix for cases that should never happen
            throw new RuntimeException(e.getMessage());
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }
}
