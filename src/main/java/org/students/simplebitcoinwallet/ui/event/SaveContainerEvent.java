package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.io.File;

@Getter
public final class SaveContainerEvent extends WalletModifierEvent {
    private final File file;
    private final String passphrase;

    public SaveContainerEvent(File file, String passphrase) {
        super(false);
        this.file = file;
        this.passphrase = passphrase;
    }
}
