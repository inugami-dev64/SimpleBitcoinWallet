package org.students.simplebitcoinwallet.ui.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * Event type for reader operations
 */
@AllArgsConstructor
@Getter
public abstract class WalletReaderEvent implements BlockingWalletEvent {
    private final Set<Integer> walletIds;
    private final boolean coloredResponse;
}
