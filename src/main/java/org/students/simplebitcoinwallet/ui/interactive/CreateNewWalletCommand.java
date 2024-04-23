package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.students.simplebitcoinwallet.ui.event.NewWalletAddressEvent;
import picocli.CommandLine.Command;

import java.io.PrintWriter;

@Command(name = "create",
        description = "Create a new wallet")
public class CreateNewWalletCommand implements Runnable {
    private final EventBus eventBus;
    private final String passphrase;

    @Inject
    public CreateNewWalletCommand(EventBus eventBus, @Named("Passphrase") String passphrase) {
        this.eventBus = eventBus;
        this.passphrase = passphrase;
    }

    @Override
    public void run() {
        eventBus.post(new NewWalletAddressEvent(passphrase, true));
    }
}
