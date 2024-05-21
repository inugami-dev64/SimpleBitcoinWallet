package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

@Getter
public final class NewWalletAddressEvent extends WalletModifierEvent {
    public NewWalletAddressEvent(boolean coloredOutput) {
        super(coloredOutput);
    }
}
