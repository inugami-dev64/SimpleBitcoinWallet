package org.students.simplebitcoinwallet;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.*;
import org.students.simplebitcoinwallet.factory.UserInteractionFactory;
import org.students.simplebitcoinwallet.ui.UserInteraction;

import java.security.Security;

public class Main {
    /**
     * Builds and returns an ArgumentParser object to use for parsing command line arguments
     * @return an ArgumentParser object
     */
    private static ArgumentParser buildParser() {
        ArgumentParser parser = ArgumentParsers.newFor("simple-bitcoin-wallet").build()
                .description("Simple Bitcoin Protocol client's wallet application");
        // the commandline utility can be used in three operational modes:
        //   create - create a new wallet file
        //   open - opens wallet file for reading in print mode (i.e non-interactive mode)
        //   interactive - opens wallet in interactive mode
        Subparsers subparsers = parser.addSubparsers()
            .dest("opmode")
            .title("Subcommands")
            .description("valid subcommands")
            .help("modes of operation");

        // create mode
        Subparser createMode = subparsers.addParser("create")
                .help("create a new wallet file");
        createMode.addArgument("filename")
                .metavar("<filename>")
                .type(String.class)
                .help("wallet file path");
        createMode.addArgument("-P")
            .required(false)
            .metavar("PASSWORD")
            .type(String.class)
            .help("wallet file password");

        // open mode
        Subparser readMode = subparsers.addParser("open")
                .help("open wallet file for reading in print mode");
        readMode.addArgument("filename")
                .metavar("<filename>")
                .type(String.class)
                .help("wallet file path");
        MutuallyExclusiveGroup workMode = readMode.addMutuallyExclusiveGroup();
        workMode.addArgument("-t")
                .type(String.class)
                .choices("sent", "received", "all")
                .help("display all transactions in which wallet's address has participated in");
        workMode.addArgument("-b")
                .action(Arguments.storeTrue())
                .help("display wallet balance");
        workMode.addArgument("-a")
                .action(Arguments.storeTrue())
                .help("create a new wallet keypair to wallet file");
        workMode.addArgument("-l")
                .action(Arguments.storeTrue())
                .help("display all wallet addresses");
        readMode.addArgument("-w", "--pick-wallet")
                .required(false)
                .metavar("N")
                .type(Integer.class)
                .help("specifies which wallet id to use for specified work mode");
        readMode.addArgument("-P")
                .required(false)
                .metavar("PASSWORD")
                .type(String.class)
                .help("wallet file password");

        // interactive mode
        Subparser interactiveMode = subparsers.addParser("interactive");
        interactiveMode.addArgument("filename")
            .metavar("<filename>")
            .type(String.class)
            .help("wallet file path");
        interactiveMode.addArgument("-P")
            .metavar("PASSWORD")
            .type(String.class)
            .help("wallet file password");

        return parser;
    }

    public static void main(String[] args) {
        // insert bouncy castle jce provider, which is required for AsymmetricCryptographyService
        Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
        ArgumentParser parser = buildParser();

        try {
            Namespace ns = parser.parseArgs(args);
            UserInteraction userInteraction = UserInteractionFactory.create((String)ns.getAttrs().get("opmode"));
            userInteraction.parseOperations(ns);
            userInteraction.run();
        }
        catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}