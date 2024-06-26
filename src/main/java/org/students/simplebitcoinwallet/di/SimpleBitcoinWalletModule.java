package org.students.simplebitcoinwallet.di;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.jline.builtins.ConfigurationPath;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.Builtins;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.widget.TailTipWidgets;
import org.students.simplebitcoinwallet.service.AsymmetricCryptographyService;
import org.students.simplebitcoinwallet.service.BitcoinNodeAPIService;
import org.students.simplebitcoinwallet.service.BlockCipherService;
import org.students.simplebitcoinwallet.service.HTTPRequestService;
import org.students.simplebitcoinwallet.service.impl.BitcoinNodeAPIServiceImpl;
import org.students.simplebitcoinwallet.service.impl.ECDSAWithSHA256CryptographyService;
import org.students.simplebitcoinwallet.service.impl.HTTPRequestServiceImpl;
import org.students.simplebitcoinwallet.service.impl.RijndaelBlockCipherService;
import org.students.simplebitcoinwallet.ui.event.listener.WalletEventListener;
import org.students.simplebitcoinwallet.ui.interactive.RootCommand;
import org.students.simplebitcoinwallet.util.LinkedListSecureContainer;
import org.students.simplebitcoinwallet.util.SecureContainer;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliCommands;
import picocli.shell.jline3.PicocliCommands.PicocliCommandsFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.function.Supplier;

/**
 * Configuration class to wire dependencies for simple bitcoin wallet API
 */
public class SimpleBitcoinWalletModule extends AbstractModule {
    private final Supplier<Path> workDir = () -> Paths.get(System.getProperty("user.dir"));

    @Provides
    public AsymmetricCryptographyService provideAsymmetricCryptographyService() {
        return new ECDSAWithSHA256CryptographyService();
    }

    @Provides
    @Named("endpoint")
    public String provideHttpEndpoint() {
        // TEMPORARY
        return "http://localhost:8080";
    }

    @Provides
    public HTTPRequestService provideHttpRequestService(@Named("endpoint") String endpointUrl) {
        return new HTTPRequestServiceImpl(endpointUrl);
    }

    @Provides
    public BitcoinNodeAPIService provideBitcoinNodeAPIService(HTTPRequestService httpRequestService) {
        return new BitcoinNodeAPIServiceImpl(httpRequestService);
    }

    @Provides
    public BlockCipherService provideBlockCipherService() {
        return new RijndaelBlockCipherService();
    }

    @Provides
    @Singleton
    public SecureContainer<KeyPair> provideSecureContainer(BlockCipherService blockCipherService) {
        return new LinkedListSecureContainer<>(blockCipherService);
    }

    @Provides
    @Singleton
    public WalletEventListener provideWalletEventListener(PrintWriter writer,
                                                          AsymmetricCryptographyService asymmetricCryptographyService,
                                                          BitcoinNodeAPIService bitcoinNodeAPIService,
                                                          SecureContainer<KeyPair> walletContainer) {
        return new WalletEventListener(writer,
                asymmetricCryptographyService,
                bitcoinNodeAPIService,
                walletContainer);
    }

    @Provides
    @Singleton
    public EventBus provideEventBus(WalletEventListener walletEventListener) {
        EventBus eventBus = new EventBus();
        eventBus.register(walletEventListener);
        return eventBus;
    }

    @Provides
    @Singleton
    public Builtins provideBuiltins(LineReader lineReader) {
        Builtins builtins = new Builtins(workDir, new ConfigurationPath(workDir.get(), workDir.get()), null);
        builtins.rename(Builtins.Command.TTOP, "top");
        builtins.alias("zle", "widget");
        builtins.alias("bindkey", "keymap");

        return builtins;
    }

    @Provides
    @Singleton
    public Terminal provideTerminal() throws IOException {
        return TerminalBuilder.builder().dumb(true).build();
    }

    @Provides
    @Singleton
    public PicocliCommands providePicocliCommands(Terminal terminal) {
        RootCommand rootCommand = new RootCommand(terminal.writer());
        PicocliCommandsFactory factory = new PicocliCommandsFactory(new GuiceFactory());
        factory.setTerminal(terminal);
        CommandLine cmd = new CommandLine(rootCommand, factory);
        return new PicocliCommands(cmd);
    }

    @Provides
    @Singleton
    public PrintWriter providePrintWriter(Terminal terminal) {
        return terminal.writer();
    }

    @Provides
    @Singleton
    public SystemRegistry provideSystemRegistry(Parser parser, Terminal terminal, Builtins builtins, PicocliCommands picocliCommands) {
        SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, workDir, null);
        systemRegistry.setCommandRegistries(builtins, picocliCommands);
        systemRegistry.register("help", picocliCommands);

        return systemRegistry;
    }

    @Provides
    @Singleton
    public Parser provideParser() {
        return new DefaultParser();
    }

    @Provides
    @Singleton
    public LineReader provideLineReader(Terminal terminal, SystemRegistry systemRegistry, Parser parser, Builtins builtins) {
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(systemRegistry.completer())
                .parser(parser)
                .variable(LineReader.LIST_MAX, 50)
                .build();

        builtins.setLineReader(reader);
        TailTipWidgets widgets = new TailTipWidgets(reader, systemRegistry::commandDescription, 5, TailTipWidgets.TipType.COMPLETER);
        widgets.enable();
        KeyMap<Binding> keyMap = reader.getKeyMaps().get("main");
        keyMap.bind(new Reference("tailtip-toggle"), KeyMap.alt("s"));
        return reader;
    }
}
