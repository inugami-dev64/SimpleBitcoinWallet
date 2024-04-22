package org.students.simplebitcoinwallet.ui.event;

public final class NewWalletAddressEvent extends WalletModifierEvent {
    public NewWalletAddressEvent(boolean coloredOutput) {
        super(coloredOutput);
    }
}
