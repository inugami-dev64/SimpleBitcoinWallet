package org.students.simplebitcoinwallet.ui.interactive;

import com.google.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.PrintWriter;

@Command(name = "",
        description = "Top level command that just prints the CLI usage",
        subcommands = {
                ShowCommand.class,
                SendTokensCommand.class,
                CreateNewWalletCommand.class
        })
public class RootCommand implements Runnable {
    private final PrintWriter out;

    @Inject
    public RootCommand(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void run() {
        out.println(new CommandLine(this).getUsageMessage());
    }
}
