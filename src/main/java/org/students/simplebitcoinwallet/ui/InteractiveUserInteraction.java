package org.students.simplebitcoinwallet.ui;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.fusesource.jansi.AnsiConsole;
import org.jline.console.SystemRegistry;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.students.simplebitcoinwallet.ui.event.InitializeContainerEvent;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;

@Command(name = "interactive", description = "Open wallet in interactive console mode")
public class InteractiveUserInteraction extends PasswordConsumer implements Runnable {
    // internal state variables
    @Parameters(index = "0", description = "Wallet file path")
    private File file;

    @Option(names = "-P", description = "Wallet password", required = false)
    private String password;

    // injected dependencies
    private final Terminal terminal;
    private final SystemRegistry systemRegistry;
    private final LineReader lineReader;
    private final EventBus eventBus;

    @Inject
    public InteractiveUserInteraction(Terminal terminal, SystemRegistry systemRegistry, LineReader lineReader, EventBus eventBus) {
        this.terminal = terminal;
        this.systemRegistry = systemRegistry;
        this.lineReader = lineReader;
        this.eventBus = eventBus;
    }

    public void run() {
        password = verifyProvidedPassword(lineReader, "Password:", password);
        eventBus.post(new InitializeContainerEvent(file, password));

        AnsiConsole.systemInstall();
        final String prompt = "[" + file.getName() + "]> ";

        String line;
        while (true) {
            try {
                systemRegistry.cleanUp();
                line = lineReader.readLine(prompt);
                systemRegistry.execute(line);
            }
            catch (UserInterruptException e) {
                terminal.writer().println("User interrupt received. Exiting...");
                AnsiConsole.systemUninstall();
                System.exit(0);
            }
            catch (EndOfFileException e) {
                AnsiConsole.systemUninstall();
                return;
            }
            catch (Exception e) {
                systemRegistry.trace(e);
            }
        }
    }
}
