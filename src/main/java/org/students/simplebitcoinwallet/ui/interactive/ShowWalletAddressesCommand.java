package org.students.simplebitcoinwallet.ui.interactive;

import com.google.common.eventbus.EventBus;
import org.students.simplebitcoinwallet.ui.event.DisplayWalletAddressesEvent;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Set;

@Command(name = "addresses",
        description = "Output data about wallet addresses")
public class ShowWalletAddressesCommand implements Runnable {
    @Option(names = "wallets", arity = "1..n", description = "Show addresses for specified wallets")
    private Set<Integer> walletIds;

    private final EventBus eventBus;
    public ShowWalletAddressesCommand(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        eventBus.post(new DisplayWalletAddressesEvent(walletIds, true));
    }
}
