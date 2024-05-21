package org.students.simplebitcoinwallet.ui;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.jline.reader.LineReader;
import org.students.simplebitcoinwallet.ui.event.NewWalletAddressEvent;
import org.students.simplebitcoinwallet.ui.event.SaveContainerEvent;
import org.students.simplebitcoinwallet.ui.event.WalletFileCreationEvent;
import org.students.simplebitcoinwallet.util.SecureContainer;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.PrintWriter;
import java.security.KeyPair;

/**
 * User interaction class for creating new a wallet file
 */
@Command(name = "create", description = "Create a new wallet")
public class WalletCreationUserInteraction extends PasswordConsumer implements Runnable {
    // internal state variables
    @Parameters(index = "0", description = "Wallet file path")
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
        eventBus.post(new WalletFileCreationEvent(file, password, true));
        eventBus.post(new SaveContainerEvent());
    }
}
