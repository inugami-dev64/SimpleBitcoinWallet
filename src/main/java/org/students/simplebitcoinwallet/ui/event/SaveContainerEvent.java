package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.io.File;

@Getter
public final class SaveContainerEvent extends WalletModifierEvent {
    private final File file;

    public SaveContainerEvent(File file) {
        super(false);
        this.file = file;
    }
}
