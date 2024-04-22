package org.students.simplebitcoinwallet.service.impl;

import org.students.simplebitcoinwallet.service.BlockCipherService;

/**
 * Dummy implementation class for BlockCipherService
 * TODO: Replace it with actual BlockCipherService implementation
 */
public class BlockCipherServiceImpl implements BlockCipherService {
    @Override
    public long checksum(byte[] msg) {
        return 0;
    }

    @Override
    public byte[] encrypt(byte[] msg, String passphrase) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] cipherMsg, String passphrase) {
        return new byte[0];
    }
}
