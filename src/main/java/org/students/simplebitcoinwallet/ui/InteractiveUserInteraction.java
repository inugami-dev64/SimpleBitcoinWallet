package org.students.simplebitcoinwallet.ui;

import net.sourceforge.argparse4j.inf.*;
import org.jline.reader.LineReader;

import java.io.Console;

public class InteractiveUserInteraction implements UserInteraction {
    private String filename;
    private String password;

    // injected dependencies
    private final Console console;
    private final LineReader lineReader;

    public InteractiveUserInteraction(Console console, LineReader lineReader) {
        this.console = console;
        this.lineReader = lineReader;
    }

    @Override
    public void parseOperations(Namespace ns) {
        filename = ns.getString("filename");
        password = ns.getString("P");

        // prompt for password if no password was specified as command line arguments
        if (password == null) {
            System.out.print("Password: ");
            System.out.flush();
            password = new String(console.readPassword());
        }
    }

    @Override
    public void run() {
        System.out.println();
        while (true) {
        }
    }
}
