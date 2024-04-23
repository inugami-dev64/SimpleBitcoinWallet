package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

@Getter
public abstract class WalletModifierEvent implements BlockingWalletEvent {
    private final boolean coloredOutput;

    WalletModifierEvent(boolean coloredOutput) {
        this.coloredOutput = coloredOutput;
    }
}
