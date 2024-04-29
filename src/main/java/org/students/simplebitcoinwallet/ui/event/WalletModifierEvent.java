package org.students.simplebitcoinwallet.ui.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class WalletModifierEvent implements BlockingWalletEvent {
    private final boolean coloredOutput;
}
