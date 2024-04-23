package org.students.simplebitcoinwallet.ui;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.jline.reader.LineReader;
import org.students.simplebitcoinwallet.ui.event.NewWalletAddressEvent;
import org.students.simplebitcoinwallet.ui.event.SaveContainerEvent;
import org.students.simplebitcoinwallet.util.SecureContainer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.Console;
import java.io.File;
import java.security.KeyPair;

/**
 * User interaction class for creating new a wallet file
 */
@Command(name = "create", description = "Create a new wallet")
public class WalletCreationUserInteraction extends PasswordConsumer implements Runnable {
    // internal state variables
    @Option(names = "-f", description = "Wallet file path")
    private File file;

    @Option(names = "-P", description = "Wallet decryption passphrase")
    private String password;

    private SecureContainer<KeyPair> wallets;

    // injected dependencies
    private final LineReader reader;
    private final EventBus eventBus;

    @Inject
    public WalletCreationUserInteraction(LineReader reader, EventBus eventBus) {
        this.reader = reader;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        password = verifyProvidedPassword(reader, "Password:", password);
        eventBus.post(new NewWalletAddressEvent(password, true));
        eventBus.post(new SaveContainerEvent(file));
    }
}
