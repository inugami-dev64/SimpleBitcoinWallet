package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.students.simplebitcoinwallet.ui.event.DisplayWalletAddressesEvent;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.HashSet;
import java.util.Set;

@Command(name = "addresses",
        description = "Output data about wallet addresses")
public class ShowWalletAddressesCommand implements Runnable {
    @Parameters(arity = "0..n", description = "Wallet ID's whose addresses to show")
    private Set<Integer> walletIds = new HashSet<>();

    private final EventBus eventBus;

    @Inject
    public ShowWalletAddressesCommand(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        eventBus.post(new DisplayWalletAddressesEvent(walletIds, true));
    }
}
