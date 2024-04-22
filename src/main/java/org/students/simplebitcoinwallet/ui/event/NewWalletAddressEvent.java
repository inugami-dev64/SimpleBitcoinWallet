package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

@Getter
public final class NewWalletAddressEvent extends WalletModifierEvent {
    private final String passphrase;

    public NewWalletAddressEvent(String passphrase, boolean coloredOutput) {
        super(coloredOutput);
        this.passphrase = passphrase;
    }
}
