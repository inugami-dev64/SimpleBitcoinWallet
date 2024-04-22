package org.students.simplebitcoinwallet.ui;

import java.io.Console;

/**
 * Base class for
 */
public abstract class PasswordConsumer {
    /**
     * Verifies if command line provided password is valid and if necessary prompts the user for a password
     * @param console specifies a console object to use for reading the password
     * @param prompt specifies the prompt message
     * @param cmdPwd specifies the command line whose validity to check
     * @return valid password string
     */
    protected String verifyProvidedPassword(Console console, String prompt, String cmdPwd) {
        if (cmdPwd == null || cmdPwd.isEmpty()) {
            System.out.print(prompt + " ");
            System.out.flush();
            return new String(console.readPassword());
        }

        return cmdPwd;
    }
}
