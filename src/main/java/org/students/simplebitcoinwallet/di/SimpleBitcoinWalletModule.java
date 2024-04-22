package org.students.simplebitcoinwallet.di;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.students.simplebitcoinwallet.service.AsymmetricCryptographyService;
import org.students.simplebitcoinwallet.service.BlockCipherService;
import org.students.simplebitcoinwallet.service.impl.BlockCipherServiceImpl;
import org.students.simplebitcoinwallet.service.impl.ECDSAWithSHA256CryptographyService;
import org.students.simplebitcoinwallet.ui.event.listener.WalletEventListener;

import java.io.Console;

/**
 * Configuration class to wire dependencies for simple bitcoin wallet API
 */
public class SimpleBitcoinWalletModule extends AbstractModule {
    @Provides
    public Console provideConsole() {
        return System.console();
    }

    @Provides
    public AsymmetricCryptographyService provideAsymmetricCryptographyService() {
        return new ECDSAWithSHA256CryptographyService();
    }

    @Provides
    public BlockCipherService provideBlockCipherService() {
        return new BlockCipherServiceImpl();
    }

    @Provides
    public EventBus provideEventBus(BlockCipherService blockCipherService, AsymmetricCryptographyService asymmetricCryptographyService) {
        WalletEventListener walletEventListener = new WalletEventListener(blockCipherService, asymmetricCryptographyService);
        EventBus eventBus = new EventBus();
        eventBus.register(walletEventListener);
        return eventBus;
    }
}
