package org.students.simplebitcoinwallet.ui.event;

import java.util.Set;

public class DisplayWalletAddressesEvent extends WalletReaderEvent {
    public DisplayWalletAddressesEvent(Set<Integer> indices, boolean coloredOutput) {
        super(indices, coloredOutput);
    }
}
