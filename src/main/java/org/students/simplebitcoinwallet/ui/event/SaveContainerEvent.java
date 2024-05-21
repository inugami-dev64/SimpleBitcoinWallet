package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

@Getter
public final class SaveContainerEvent extends WalletModifierEvent {
    public SaveContainerEvent() {
        super(false);
    }
}
