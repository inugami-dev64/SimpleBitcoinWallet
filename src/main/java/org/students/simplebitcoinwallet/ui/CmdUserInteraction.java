package org.students.simplebitcoinwallet.ui;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.students.simplebitcoinwallet.ui.event.*;
import org.students.simplebitcoinwallet.util.SecureContainer;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.Console;
import java.io.File;
import java.security.KeyPair;
import java.util.Set;

/**
 * CmdUserInteraction class represents non-interactive wallet interaction ('open' work-mode), i.e actions are performed according to specified flags
 * and results are printed to stdout. User input reading is unnecessary except for cases when passphrase needs to be read.
 */
@Command(name = "open", description = "Open a wallet file for reading in command line mode")
public class CmdUserInteraction extends PasswordConsumer implements Runnable {
    // internal state variables
    @Option(names = "-f", required = true, description = "Wallet file path")
    private File file;

    @Option(names = "-P", required = false, description = "Wallet decryption password")
    private String password;

    @ArgGroup(exclusive = true, multiplicity = "1")
    private WorkMode workMode;

    static class WorkMode {
        @Option(names = "-t", description = "Display transactions")
        TransactionFilter transactionFilter;
        @Option(names = "-b", description = "Display wallet balance")
        boolean displayBalance;
        @Option(names = "-a", description = "Generate a new wallet address")
        boolean generateNewWalletAddress;
        @Option(names = "-l", description = "List all wallet addresses")
        boolean listAllWalletAddresses;
    }

    @Option(names = {"-w", "--pick-wallet"}, required = false, arity = "1..n")
    private Set<Integer> walletIds;

    private SecureContainer<KeyPair> wallets;

    // injected dependencies
    private final Console console;
    private final EventBus eventBus;

    @Inject
    public CmdUserInteraction(Console console, EventBus eventBus) {
        this.console = console;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        password = verifyProvidedPassword(console, "Password:", password);
        eventBus.post(new InitializeContainerEvent(file, password));

        // determine current work mode and thus appropriate function calls
        if (workMode.transactionFilter != null)
            eventBus.post(new TransactionsEvent(workMode.transactionFilter, walletIds, true));
        else if (workMode.displayBalance)
            eventBus.post(new BalanceEvent(walletIds, true));
        else if (workMode.generateNewWalletAddress)
            eventBus.post(new NewWalletAddressEvent(password,true));
        else if (workMode.listAllWalletAddresses)
            eventBus.post(new DisplayWalletAddressesEvent(walletIds, true));

        eventBus.post(new SaveContainerEvent(file));
    }

    private void showTransactions() {
        // TODO: Implement functionality to show transactions.
        //       If walletId is not null then show transaction for given wallet,
        //       otherwise show transactions for all wallets
        System.out.println("CmdUserInteraction.showTransactions called!");
    }

    private void listWallets() {
        // TODO: List all wallet addresses deserialized from given file
        System.out.println("CmdUserInteraction.listWallets called!");
    }

    private void displayBalance() {
        // TODO: Implement functionality to display wallet balance(s).
        //       If walletId is not null then show balance for given wallet,
        //       otherwise show wallet balances for all wallets
        System.out.println("CmdUserInteraction.displayBalance called!");
    }

    private void addNewWallet() {
        // TODO: Implement functionality to add a new wallet address to wallet storage.
        System.out.println("CmdUserInteraction.addNewWallet called!");
    }
}
