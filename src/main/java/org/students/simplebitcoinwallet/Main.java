package org.students.simplebitcoinwallet;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

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

        // open mode
        Subparser readMode = subparsers.addParser("open")
                .help("open wallet file for reading in print mode");
        readMode.addArgument("filename")
                .metavar("<filename>")
                .type(String.class)
                .help("wallet file path");
        MutuallyExclusiveGroup workMode = readMode.addMutuallyExclusiveGroup();
        workMode.addArgument("-t")
                .choices("sent", "received", "all")
                .help("display all transactions in which wallet's address has participated in");
        workMode.addArgument("-b")
                .metavar("")
                .help("display wallet balance");
        readMode.addArgument("-w", "--pick-wallet")
                .metavar("")
                .help("specifies which wallet id to use for specified work mode");

        // interactive mode
        Subparser interactiveMode = subparsers.addParser("interactive");
        interactiveMode.addArgument("filename")
                .metavar("<filename>")
                .type(String.class)
                .help("wallet file path");

        return parser;
    }

    public static void main(String[] args) {
        // insert bouncy castle jce provider, which is required for AsymmetricCryptographyService
        Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
        ArgumentParser parser = buildParser();

        try {
            parser.parseArgs(args);
        }
        catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}