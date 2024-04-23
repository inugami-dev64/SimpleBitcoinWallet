package org.students.simplebitcoinwallet.ui;

import com.google.inject.Inject;
import org.fusesource.jansi.AnsiConsole;
import org.jline.console.SystemRegistry;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.students.simplebitcoinwallet.di.PassphraseFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "interactive", description = "Open wallet in interactive console mode")
public class InteractiveUserInteraction extends PasswordConsumer implements Runnable {
    // internal state variables
    @Option(names = "-f", description = "Wallet file path", required = true)
    private String filename;

    @Option(names = "-P", description = "Wallet password", required = false)
    private String password;

    // injected dependencies
    private final Terminal terminal;
    private final SystemRegistry systemRegistry;
    private final LineReader lineReader;

    @Inject
    public InteractiveUserInteraction(Terminal terminal, SystemRegistry systemRegistry, LineReader lineReader) {
        this.terminal = terminal;
        this.systemRegistry = systemRegistry;
        this.lineReader = lineReader;
    }

    public void run() {
        password = verifyProvidedPassword(lineReader, "Password:", password);
        PassphraseFactory.setPassphrase(password);

        AnsiConsole.systemInstall();
        final String prompt = "[" + filename + "]> ";

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
