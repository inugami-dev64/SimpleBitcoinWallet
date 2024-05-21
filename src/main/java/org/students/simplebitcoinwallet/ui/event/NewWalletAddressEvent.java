package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.io.PrintWriter;

@Getter
public final class NewWalletAddressEvent extends WalletModifierEvent {
    public NewWalletAddressEvent(boolean coloredOutput) {
        super(coloredOutput);
    }
}
