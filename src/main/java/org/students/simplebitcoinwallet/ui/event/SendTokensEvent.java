package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.Supplier;

public class SendTokensEvent extends WalletModifierEvent {
    @Getter
    private String recipientAddress;
    private Supplier<Boolean> promptDoubleCheck;

    public SendTokensEvent(String recipient, BigDecimal amount, boolean coloredOutput, Supplier<Boolean> promptDoubleCheck) {
        super(coloredOutput);
    }

    public boolean doubleCheck() {
        if (promptDoubleCheck != null)
            return promptDoubleCheck.get();
        return true;
    }
}
