package org.students.simplebitcoinwallet.di;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

/**
 * Factory class for picocli to work with Guice registered dependencies
 */
public class GuiceFactory implements IFactory {
    private final Injector injector = Guice.createInjector(new SimpleBitcoinWalletModule());

    @Override
    public <K> K create(Class<K> type) throws Exception {
        try {
            return injector.getInstance(type);
        }
        catch (ConfigurationException e) {
            return CommandLine.defaultFactory().create(type);
        }
    }
}
