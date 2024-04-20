package org.students.simplebitcoinwallet.factory;

import org.students.simplebitcoinwallet.service.impl.ECDSAWithSHA256CryptographyService;
import org.students.simplebitcoinwallet.ui.CmdUserInteraction;
import org.students.simplebitcoinwallet.ui.InteractiveUserInteraction;
import org.students.simplebitcoinwallet.ui.UserInteraction;
import org.students.simplebitcoinwallet.ui.WalletCreationUserInteraction;

/**
 * Factory class for creating UserInteraction objects
 */
public class UserInteractionFactory {
    /**
     * Create a new UserInteraction object, whose type is determined by opmode
     * @param opmode specifies the string value representing the type of the UserInteraction object
     * @return a valid UserInteraction object, if opmode was valid, null otherwise
     */
    public static UserInteraction create(String opmode) {
        return switch (opmode) {
            case "create" -> new WalletCreationUserInteraction(System.console());
            case "open" -> new CmdUserInteraction(System.console());
            case "interactive" -> new InteractiveUserInteraction(System.console());
            default -> null;
        };
    }
}
