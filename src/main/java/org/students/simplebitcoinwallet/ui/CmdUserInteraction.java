package org.students.simplebitcoinwallet.ui;

import net.sourceforge.argparse4j.inf.Namespace;

import java.io.Console;
import java.util.Map;

/**
 * CmdUserInteraction class represents non-interactive wallet interaction ('open' work-mode), i.e actions are performed according to specified flags
 * and results are printed to stdout. User input reading is unnecessary except for cases when passphrase needs to be read.
 */
public class CmdUserInteraction implements UserInteraction {
    // internal state variables
    private String filename;
    private String password;
    private WorkModeOperation operation;
    private TransactionFilter transactionFilter;
    private Integer walletId;

    // injected dependencies
    private final Console console;

    public CmdUserInteraction(Console console) {
        this.console = console;
    }

    @Override
    public void parseOperations(Namespace ns) {
        this.filename = ns.getString("filename");
        if (ns.getInt("w") != null)
            this.walletId = ns.getInt("w");
        else if (ns.getInt("pick-wallet") != null)
            this.walletId = ns.getInt("pick-wallet");

        // try reading password from the commandline arguments and if necessary ask user directly
        this.password = ns.getString("P");
        if (this.password == null) {
            System.out.print("Password: ");
            System.out.flush();
            this.password = new String(console.readPassword());
        }

        // parse the work mode
        if (ns.getString("t") != null) {
            operation = WorkModeOperation.SHOW_TRANSACTIONS;
            transactionFilter = TransactionFilter.valueOf(ns.getString("t").toUpperCase());
        }
        else if (ns.getBoolean("b"))
            operation = WorkModeOperation.DISPLAY_BALANCE;
        else if (ns.getBoolean("a"))
            operation = WorkModeOperation.ADD_NEW_WALLET;
        else if (ns.getBoolean("l"))
            operation = WorkModeOperation.LIST_WALLETS;
    }

    @Override
    public void run() {
        switch (operation) {
            case SHOW_TRANSACTIONS:
                showTransactions();
                break;
            case LIST_WALLETS:
                listWallets();
                break;

            case DISPLAY_BALANCE:
                displayBalance();
                break;

            case ADD_NEW_WALLET:
                addNewWallet();
                break;
        }
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
