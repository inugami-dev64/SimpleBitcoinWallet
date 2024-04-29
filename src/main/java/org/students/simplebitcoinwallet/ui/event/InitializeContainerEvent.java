package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.io.File;

@Getter
public final class InitializeContainerEvent extends WalletModifierEvent {
    private final File file;
    private final String passphrase;

    public InitializeContainerEvent(File file, String passphrase) {
        super(false);
        this.file = file;
        this.passphrase = passphrase;
    }
}
