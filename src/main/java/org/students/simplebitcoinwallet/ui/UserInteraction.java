package org.students.simplebitcoinwallet.ui;

import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Interface for describing base functionality for different operational modes
 */
public interface UserInteraction {
    /**
     * Parses flags and sets the internal state of the user interaction class
     * @param ns specifies the namespace whose arguments to use for parsing
     */
    void parseOperations(Namespace ns);

    /**
     * Run user interaction with current internal states
     */
    void run();
}
