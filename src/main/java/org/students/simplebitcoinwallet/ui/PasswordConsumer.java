package org.students.simplebitcoinwallet.ui;

import org.jline.reader.LineReader;

/**
 * Base class for
 */
public abstract class PasswordConsumer {
    /**
     * Verifies if command line provided password is valid and if necessary prompts the user for a password
     * @param reader specifies a LineReader object to use for reading the password
     * @param prompt specifies the prompt message
     * @param cmdPwd specifies the command line whose validity to check
     * @return valid password string
     */
    protected String verifyProvidedPassword(LineReader reader, String prompt, String cmdPwd) {
        if (cmdPwd == null || cmdPwd.isEmpty()) {
            return reader.readLine(prompt + " ", '*').strip();
        }

        return cmdPwd;
    }
}
