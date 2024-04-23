package org.students.simplebitcoinwallet.util;

import org.students.simplebitcoinwallet.service.BlockCipherService;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class LinkedListSecureContainer<T> extends SecureContainer<T> {
    List<T> innerContainer = new LinkedList<>();

    public LinkedListSecureContainer(BlockCipherService blockCipherService, String passphrase) {
        super(blockCipherService, passphrase);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        innerContainer.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return innerContainer.spliterator();
    }

    @Override
    public Iterator<T> iterator() {
        return innerContainer.iterator();
    }

    @Override
    public T get(int n) {
        return innerContainer.get(n);
    }

    @Override
    public boolean remove(T obj) {
        return innerContainer.remove(obj);
    }

    @Override
    public boolean add(T obj) {
        return innerContainer.add(obj);
    }

    @Override
    public int size() {
        return innerContainer.size();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream))
        {
            objectOutput.writeObject(innerContainer);
            byte[] msg = byteArrayOutputStream.toByteArray();
            final long checksum = blockCipherService.checksum(msg);
            byte[] cipher = blockCipherService.encrypt(msg, passphrase);
            out.writeLong(checksum);
            out.writeInt(cipher.length);
            out.write(cipher);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        final long checksum = in.readLong();
        final int size = in.readInt();

        byte[] cipher = new byte[size];
        in.read(cipher);

        byte[] rawMsg = blockCipherService.decrypt(cipher, passphrase);
        if (blockCipherService.checksum(rawMsg) != checksum)
            throw new IOException("Invalid passphrase");

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawMsg);
             ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream))
        {
            Object rawObj = objectInput.readObject();
            if (rawObj instanceof List)
                innerContainer = (List<T>) objectInput.readObject();
        }
    }
}
