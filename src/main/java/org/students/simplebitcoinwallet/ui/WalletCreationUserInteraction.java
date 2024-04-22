package org.students.simplebitcoinwallet.ui;

import com.google.inject.Inject;
import org.students.simplebitcoinwallet.service.AsymmetricCryptographyService;
import org.students.simplebitcoinwallet.service.BlockCipherService;
import org.students.simplebitcoinwallet.util.SecureContainer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.Console;
import java.security.KeyPair;

/**
 * User interaction class for creating new a wallet file
 */
@Command(name = "create", description = "Create a new wallet")
public class WalletCreationUserInteraction implements Runnable {
    // internal state variables
    @Option(names = "-f", description = "Wallet file path")
    private String filename;

    @Option(names = "-P", description = "Wallet decryption passphrase")
    private String password;

    private SecureContainer<KeyPair> wallets;

    // injected dependencies
    private final Console console;
    private final AsymmetricCryptographyService asymmetricCryptographyService;
    private final BlockCipherService blockCipherService;

    @Inject
    public WalletCreationUserInteraction(Console console, AsymmetricCryptographyService asymmetricCryptographyService, BlockCipherService blockCipherService) {
        this.console = console;
        this.asymmetricCryptographyService = asymmetricCryptographyService;
        this.blockCipherService = blockCipherService;
    }

    @Override
    public void run() {
        // ask for user password if not specified
        if (password == null) {
            System.out.print("New password for " + filename + ": ");
            System.out.flush();
            password = new String(console.readPassword());
        }

        // TODO: Implement wallet key generation and saving into encrypted file
    }
}
