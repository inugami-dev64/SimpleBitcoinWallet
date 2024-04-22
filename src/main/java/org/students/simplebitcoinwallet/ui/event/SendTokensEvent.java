package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.Supplier;

@Getter
public class SendTokensEvent extends WalletModifierEvent {
    private final String recipientAddress;
    private final BigDecimal amount;
    private final Supplier<Boolean> promptDoubleCheck;

    public SendTokensEvent(String recipient, BigDecimal amount, Supplier<Boolean> promptDoubleCheck, boolean coloredOutput) {
        super(coloredOutput);
        this.recipientAddress = recipient;
        this.amount = amount;
        this.promptDoubleCheck = promptDoubleCheck;
    }

    public boolean doubleCheck() {
        if (promptDoubleCheck != null)
            return promptDoubleCheck.get();
        return true;
    }
}
