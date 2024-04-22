package org.students.simplebitcoinwallet.ui.event;

import java.util.Set;

/**
 * Event to request wallet balance
 */
public class BalanceEvent extends WalletReaderEvent {
    public BalanceEvent(Set<Integer> walletIds, boolean coloredOutput) {
        super(walletIds, coloredOutput);
    }
}
