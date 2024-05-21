package org.students.simplebitcoinwallet.ui.event;

import lombok.Getter;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.function.Function;

@Getter
public class SendTokensEvent extends WalletModifierEvent {
    private final Integer from;
    private final String recipientAddress;
    private final BigDecimal amount;
    private final Function<String, Boolean> promptDoubleCheck;

    public SendTokensEvent(Integer from, String recipient, BigDecimal amount, Function<String, Boolean> promptDoubleCheck, boolean coloredOutput) {
        super(coloredOutput);
        this.from = from;
        this.recipientAddress = recipient;
        this.amount = amount;
        this.promptDoubleCheck = promptDoubleCheck;
    }

    public boolean doubleCheck(String fromAddress) {
        if (promptDoubleCheck != null)
            return promptDoubleCheck.apply(fromAddress);
        return true;
    }
}
