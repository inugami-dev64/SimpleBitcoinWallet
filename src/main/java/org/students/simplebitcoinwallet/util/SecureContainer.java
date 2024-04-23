package org.students.simplebitcoinwallet.util;

import org.students.simplebitcoinwallet.service.BlockCipherService;

import java.io.Externalizable;

/**
 * Secure (wrapper) container class which can be serialized in an encrypted form.<br>
 * This container should be used for managing wallet keys and other persistent sensitive information.
 */
public abstract class SecureContainer<T> implements Externalizable, Iterable<T> {
    // internal state variables
    protected String passphrase = null;

    // injected dependencies
    protected final BlockCipherService blockCipherService;

    public SecureContainer(BlockCipherService blockCipherService, String passphrase) {
        this.blockCipherService = blockCipherService;
        this.passphrase = passphrase;
    }

    /**
     * Retrieves n'th wallet from the wallet container
     * @param n specifies the given index, which to use for retrieving given wallet
     * @return an instance containing the n'th wallet's keys
     */
    public abstract T get(int n);

    /**
     * Remove provided object from the container
     * @param obj specifies the object
     * @return true of removal was successful, false otherwise
     */
    public abstract boolean remove(T obj);

    /**
     * Add a new object to the container
     * @param obj specifies the object to add to the container
     * @return true if adding was successful, false otherwise
     */
    public abstract boolean add(T obj);

    /**
     * Get the amount of wallets contained in given container
     * @return number of wallets contained in the container
     */
    public abstract int size();
}
