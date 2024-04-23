package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.jline.reader.LineReader;
import org.students.simplebitcoinwallet.ui.event.SendTokensEvent;
import org.students.simplebitcoinwallet.util.Colored;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.PrintWriter;
import java.math.BigDecimal;

@Command(name = "send",
        description = "Send tokens to another address")
public class SendTokensCommand implements Runnable {
    @Option(names = "from", description = "Source wallet where to send tokens from", required = true)
    private Integer from;

    @Option(names = "to", description = "Recipient address who receives sent tokens", required = true)
    private String recipient;

    @Option(names = "amount", description = "Amount of tokens to send", required = true)
    private BigDecimal amount;

    // injected dependencies
    private final EventBus eventBus;
    private final PrintWriter printWriter;
    private final LineReader lineReader;

    @Inject
    public SendTokensCommand(EventBus eventBus, PrintWriter printWriter, LineReader lineReader) {
        this.eventBus = eventBus;
        this.printWriter = printWriter;
        this.lineReader = lineReader;
    }

    @Override
    public void run() {
        eventBus.post(new SendTokensEvent(from, recipient, amount, this::doubleCheckPrompt, true));
    }

    public boolean doubleCheckPrompt(String fromAddress) {
        printWriter.println("Are you sure you want to perform following transactions?");
        printWriter.println("SENT TOKENS ARE NOT REFUNDABLE!!!");
        printWriter.println("Sender: " + Colored.ANSI_YELLOW + fromAddress + Colored.ANSI_RESET);
        printWriter.println("Recipient: " + Colored.ANSI_GREEN + recipient + Colored.ANSI_RESET);
        printWriter.println("Amount: " + amount);
        return lineReader.readLine("Type YES (all caps) to sign the transaction: ").strip().equals("YES");
    }
}
