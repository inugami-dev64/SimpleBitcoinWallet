package org.students.simplebitcoinwallet.ui;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.*;

@Command(name = "interactive", description = "Open wallet in interactive console mode")
public class InteractiveUserInteraction extends PasswordConsumer implements Runnable {
    // internal state variables
    @Option(names = "-f", description = "Wallet file path", required = true)
    private String filename;

    @Option(names = "-P", description = "Wallet password", required = false)
    private String password;

    // injected dependencies
    private final Console console;

    @Inject
    public InteractiveUserInteraction(Console console, EventBus eventBus) {
        this.console = console;
    }

    public void run() {
        // ask for password if necessary
        if (password == null) {
            System.out.print("Password: ");
            System.out.flush();
            password = new String(console.readPassword());
        }

        final String prompt = "[" + filename + "]> ";
        System.out.println(prompt);
    }

    /*
    private boolean readWalletKeys() {
        wallets = new LinkedListSecureContainer<>(blockCipherService, filename);
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filename));
             ObjectInput objectInput = new ObjectInputStream(bufferedInputStream))
        {
            wallets.readExternal(objectInput);
            return true;
        }
        catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            return false;
        }
    }*/
}
