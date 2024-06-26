package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;
import org.students.simplebitcoinwallet.data.TransactionQueryType;

import java.util.Set;
import java.io.PrintWriter;

/**
 * Type of event that signals human-readable transaction objects must be returned
 */
@Getter
public final class TransactionsEvent extends WalletReaderEvent {
    private final TransactionQueryType transactionQueryType;

    public TransactionsEvent(TransactionQueryType transactionQueryType, Set<Integer> walletIds, boolean coloredOutput) {
        super(walletIds, coloredOutput);
        this.transactionQueryType = transactionQueryType;
    }
}
