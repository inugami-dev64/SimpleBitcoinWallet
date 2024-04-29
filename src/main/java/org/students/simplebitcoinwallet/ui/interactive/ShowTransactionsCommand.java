package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.students.simplebitcoinwallet.ui.TransactionFilter;
import org.students.simplebitcoinwallet.ui.event.TransactionsEvent;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Set;

@Command(name = "transactions",
        description = "Output data about transactions")
public class ShowTransactionsCommand implements Runnable {
    @Option(names = "filter", description = "Filter transactions by type", required = true)
    private TransactionFilter transactionFilter;

    @Option(names = "wallets", arity = "1..n", description = "Show transactions for specified wallets", required = false)
    private Set<Integer> walletIds;

    // injected dependencies
    private final EventBus eventBus;

    @Inject
    public ShowTransactionsCommand(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        eventBus.post(new TransactionsEvent(transactionFilter, walletIds, true));
    }
}