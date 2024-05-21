package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.students.simplebitcoinwallet.ui.event.NewWalletAddressEvent;
import org.students.simplebitcoinwallet.ui.event.SaveContainerEvent;
import picocli.CommandLine.Command;

@Command(name = "create",
        description = "Create a new wallet")
public class CreateNewWalletCommand implements Runnable {
    private final EventBus eventBus;

    @Inject
    public CreateNewWalletCommand(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        eventBus.post(new NewWalletAddressEvent(true));
        eventBus.post(new SaveContainerEvent());
    }
}
