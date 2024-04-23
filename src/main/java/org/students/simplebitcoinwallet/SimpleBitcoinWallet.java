package org.students.simplebitcoinwallet;

import org.students.simplebitcoinwallet.di.GuiceFactory;
import org.students.simplebitcoinwallet.ui.CmdUserInteraction;
import org.students.simplebitcoinwallet.ui.InteractiveUserInteraction;
import org.students.simplebitcoinwallet.ui.WalletCreationUserInteraction;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.security.Security;

@Command(name = "simple-bitcoin-wallet",
        mixinStandardHelpOptions = true,
        version = "Simple bitcoin wallet app v1.0",
        subcommands = {
            InteractiveUserInteraction.class,
            CmdUserInteraction.class,
            WalletCreationUserInteraction.class
        },
        subcommandsRepeatable = false
)
public class SimpleBitcoinWallet implements Runnable {
    @Override
    public void run() {}

    public static void main(String[] args) {
        // insert bouncy castle jce provider, which is required for AsymmetricCryptographyService
        Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
        int exitCode = new CommandLine(SimpleBitcoinWallet.class, new GuiceFactory()).execute(args);
        System.exit(exitCode);
    }
}