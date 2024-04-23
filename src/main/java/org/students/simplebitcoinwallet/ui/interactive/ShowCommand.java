package org.students.simplebitcoinwallet.ui.interactive;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.PrintWriter;

@Command(name = "show",
        description = "Output data about wallets",
        subcommands = {
                ShowTransactionsCommand.class,
                ShowBalanceCommand.class,
                ShowWalletAddressesCommand.class,
        })
public class ShowCommand implements Runnable {
    private final PrintWriter out;
    private ShowCommand(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void run() {
        out.println(new CommandLine(this).getUsageMessage());
    }
}
