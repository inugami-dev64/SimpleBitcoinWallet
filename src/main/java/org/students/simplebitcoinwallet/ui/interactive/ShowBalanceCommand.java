package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.students.simplebitcoinwallet.ui.event.BalanceEvent;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.HashSet;
import java.util.Set;

@Command(name = "balance",
        description = "Output data about wallet balances")
public class ShowBalanceCommand implements Runnable {
    @Parameters(arity = "0..n", description = "Show balances for specified wallets")
    private Set<Integer> walletIds = new HashSet<>();

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