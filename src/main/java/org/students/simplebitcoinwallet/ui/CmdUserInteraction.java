package org.students.simplebitcoinwallet.ui;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.jline.reader.LineReader;
import org.students.simplebitcoinwallet.data.TransactionQueryType;
import org.students.simplebitcoinwallet.ui.event.*;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.Set;

/**
 * CmdUserInteraction class represents non-interactive wallet interaction ('open' work-mode), i.e actions are performed according to specified flags
 * and results are printed to stdout. User input reading is unnecessary except for cases when passphrase needs to be read.
 */
@Command(name = "open", description = "Open a wallet file for reading in command line mode")
public class CmdUserInteraction extends PasswordConsumer implements Runnable {
    // internal state variables
    @Parameters(index = "0", description = "Wallet file")
    private File file;

    @Option(names = "-P", required = false, description = "Wallet encryption password")
    private String password;

    @ArgGroup(exclusive = true, multiplicity = "1")
    private WorkMode workMode;

    static class WorkMode {
        @Option(names = "-t", description = "Display transactions")
        TransactionQueryType transactionQueryType;
        @Option(names = "-b", description = "Display wallet balance")
        boolean displayBalance;
        @Option(names = "-a", description = "Generate a new wallet address")
        boolean generateNewWalletAddress;
        @Option(names = "-l", description = "List all wallet addresses")
        boolean listAllWalletAddresses;
    }

    @Option(names = {"-w", "--pick-wallet"}, required = false, arity = "1..n")
    private Set<Integer> walletIds;

    // injected dependencies
    private final LineReader reader;
    private final EventBus eventBus;

    @Inject
    public CmdUserInteraction(LineReader reader, EventBus eventBus) {
        this.reader = reader;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        password = verifyProvidedPassword(reader, "Password:", password);
        eventBus.post(new InitializeContainerEvent(file, password));

        // determine current work mode and thus appropriate function calls
        if (workMode.transactionQueryType != null)
            eventBus.post(new TransactionsEvent(workMode.transactionQueryType, walletIds, true));
        else if (workMode.displayBalance)
            eventBus.post(new BalanceEvent(walletIds, true));
        else if (workMode.generateNewWalletAddress)
            eventBus.post(new NewWalletAddressEvent(true));
        else if (workMode.listAllWalletAddresses)
            eventBus.post(new DisplayWalletAddressesEvent(walletIds, true));

        eventBus.post(new SaveContainerEvent());
    }
}
