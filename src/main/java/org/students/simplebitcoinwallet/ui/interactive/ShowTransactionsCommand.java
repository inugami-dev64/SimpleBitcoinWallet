package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.students.simplebitcoinwallet.data.TransactionQueryType;
import org.students.simplebitcoinwallet.ui.event.TransactionsEvent;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.HashSet;
import java.util.Set;

@Command(name = "transactions",
        description = "Output data about transactions")
public class ShowTransactionsCommand implements Runnable {
    @Parameters(arity = "0..n", description = "Show transactions for only given wallets")
    private Set<Integer> walletIds = new HashSet<>();

    @Option(names = "filter", description = "Filter transactions by type", required = false, defaultValue = "ALL")
    private TransactionQueryType transactionQueryType;

    // injected dependencies
    private final EventBus eventBus;

    @Inject
    public ShowTransactionsCommand(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        eventBus.post(new TransactionsEvent(transactionQueryType, walletIds, true));
    }
}