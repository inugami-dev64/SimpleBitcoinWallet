package org.students.simplebitcoinwallet.ui;

import net.sourceforge.argparse4j.inf.Namespace;
import java.io.Console;

/**
 * User interaction class for creating new a wallet file
 */
public class WalletCreationUserInteraction implements UserInteraction {
    private String walletFileName = null;
    private String password = null;
    private final Console console;

    public WalletCreationUserInteraction(Console console) {
        this.console = console;
    }

    @Override
    public void parseOperations(Namespace ns) {
        walletFileName = ns.getString("filename");
        password = ns.getString("P");

        // in case the password was not specified with -P flag
        if (password == null) {
            System.out.print("Password: ");
            System.out.flush();
            password = new String(console.readPassword());
        }
    }

    @Override
    public void run() {
        // TODO: Implement wallet key generation and saving into encrypted file
    }
}
