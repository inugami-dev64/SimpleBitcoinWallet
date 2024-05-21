package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.io.File;
import java.io.PrintWriter;

@Getter
public final class SaveContainerEvent extends WalletModifierEvent {
    public SaveContainerEvent() {
        super(false);
    }
}
