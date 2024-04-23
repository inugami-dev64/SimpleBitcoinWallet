package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.students.simplebitcoinwallet.ui.event.BalanceEvent;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.PrintWriter;
import java.util.Set;

@Command(name = "balance",
        description = "Output data about wallet balances")
public class ShowBalanceCommand implements Runnable {
    @Option(names = "wallets", arity = "1..n", description = "Show balances for specified wallets", required = false)
    private Set<Integer> walletIds;

    // injected dependencies
    private final EventBus eventBus;

    @Inject
    public ShowBalanceCommand(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        eventBus.post(new BalanceEvent(walletIds, true));
    }
}