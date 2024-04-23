package org.students.simplebitcoinwallet.ui.event.listener;

import com.google.common.eventbus.Subscribe;
import org.students.simplebitcoinwallet.service.AsymmetricCryptographyService;
import org.students.simplebitcoinwallet.service.BlockCipherService;
import org.students.simplebitcoinwallet.ui.event.*;
import org.students.simplebitcoinwallet.util.Colored;
import org.students.simplebitcoinwallet.util.Encoding;
import org.students.simplebitcoinwallet.util.LinkedListSecureContainer;
import org.students.simplebitcoinwallet.util.SecureContainer;

import java.io.*;
import java.security.KeyPair;

/**
 * Wallet UI event listeners which handle
 */
public class WalletEventListener {
    private SecureContainer<KeyPair> walletContainer;

    // injected dependencies
    private final BlockCipherService blockCipherService;
    private final AsymmetricCryptographyService asymmetricCryptographyService;
    private final PrintWriter writer;

    public WalletEventListener(PrintWriter writer, BlockCipherService blockCipherService, AsymmetricCryptographyService asymmetricCryptographyService) {
        this.writer = writer;
        this.blockCipherService = blockCipherService;
        this.asymmetricCryptographyService = asymmetricCryptographyService;
    }


    @Subscribe
    public void handleContainerInitializationEvent(InitializeContainerEvent event) {
        walletContainer = new LinkedListSecureContainer<>(blockCipherService, event.getPassphrase());
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(event.getFile()));
             ObjectInput objectInput = new ObjectInputStream(bufferedInputStream))
        {
            walletContainer.readExternal(objectInput);
        }
        catch (Exception e) {
            writer.println("ERROR: " + e.getMessage());
            writer.flush();
            System.exit(1);
        }
    }

    @Subscribe
    public void handleSaveContainerEvent(SaveContainerEvent event) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(event.getFile()));
             ObjectOutput objectOutput = new ObjectOutputStream(bufferedOutputStream))
        {
            walletContainer.writeExternal(objectOutput);
        }
        catch (Exception e) {
            writer.println(Colored.ANSI_RED + "ERROR: " + e.getMessage() + Colored.ANSI_RESET);
            writer.flush();
            System.exit(1);
        }
    }

    @Subscribe
    public void handleBalanceEvent(BalanceEvent event) {
        // TODO: Implement wallet balance output.
    }

    @Subscribe
    public void handleDisplayWalletAddressesEvent(DisplayWalletAddressesEvent event) {
        // TODO: Implement wallet address output.
    }

    @Subscribe
    public void handleNewWalletAddressEvent(NewWalletAddressEvent event) {
        walletContainer = new LinkedListSecureContainer<>(blockCipherService, event.getPassphrase());
        KeyPair newKeyPair = asymmetricCryptographyService.generateNewKeypair();
        if (event.isColoredOutput()) {
            writer.println("Generated a new wallet with address:\n" +
                                Colored.ANSI_YELLOW +
                                Encoding.defaultPubKeyEncoding(newKeyPair.getPublic().getEncoded()) +
                                Colored.ANSI_RESET);
            writer.flush();
        }
        else {
            writer.println("Generate a new wallet with address:\n" +
                               Encoding.defaultPubKeyEncoding(newKeyPair.getPublic().getEncoded()));
            writer.flush();
        }

        walletContainer.add(newKeyPair);
    }

    @Subscribe
    public void handleSendTokensEvent(SendTokensEvent event) {
        if (event.doubleCheck("wallet address")) {
            // TODO: Implement token sending to another public key address logic
        }
    }

    @Subscribe
    public void handleTransactionsEvent(TransactionsEvent event) {
        // TODO: Implement transactions output
        switch (event.getTransactionFilter()) {
            case ALL: break;
            case SENT: break;
            case RECEIVED: break;
        }
    }
}
