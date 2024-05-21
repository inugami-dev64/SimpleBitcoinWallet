package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.io.File;

@Getter
public class WalletFileCreationEvent extends WalletModifierEvent {
    private final File file;
    private final String passphrase;

    public WalletFileCreationEvent(File file, String passphrase, boolean coloredOutput) {
        super(true);
        this.file = file;
        this.passphrase = passphrase;
    }
}
