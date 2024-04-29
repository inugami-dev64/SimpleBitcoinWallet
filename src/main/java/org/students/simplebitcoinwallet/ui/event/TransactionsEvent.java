package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;
import org.students.simplebitcoinwallet.ui.TransactionFilter;

import java.util.Set;

/**
 * Type of event that signals human-readable transaction objects must be returned
 */
@Getter
public final class TransactionsEvent extends WalletReaderEvent {
    private final TransactionFilter transactionFilter;

    public TransactionsEvent(TransactionFilter filter, Set<Integer> walletIds, boolean coloredOutput) {
        super(walletIds, coloredOutput);
        this.transactionFilter = filter;
    }
}
