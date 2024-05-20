package org.students.simplebitcoinwallet.ui.event.listener;

import com.google.common.eventbus.Subscribe;
import org.students.simplebitcoinwallet.data.Transaction;
import org.students.simplebitcoinwallet.data.TransactionOutput;
import org.students.simplebitcoinwallet.data.TransactionQueryType;
import org.students.simplebitcoinwallet.exception.ExternalNodeInvalidHTTPCodeException;
import org.students.simplebitcoinwallet.service.AsymmetricCryptographyService;
import org.students.simplebitcoinwallet.service.BitcoinNodeAPIService;
import org.students.simplebitcoinwallet.service.BlockCipherService;
import org.students.simplebitcoinwallet.ui.event.*;
import org.students.simplebitcoinwallet.util.Colored;
import org.students.simplebitcoinwallet.util.Encoding;
import org.students.simplebitcoinwallet.util.LinkedListSecureContainer;
import org.students.simplebitcoinwallet.util.SecureContainer;

import java.io.*;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Wallet UI event listeners which handle
 */
public class WalletEventListener {
    private SecureContainer<KeyPair> walletContainer;

    // injected dependencies
    private final BlockCipherService blockCipherService;
    private final AsymmetricCryptographyService asymmetricCryptographyService;
    private final PrintWriter writer;
    private final BitcoinNodeAPIService bitcoinNodeAPIService;

    public WalletEventListener(PrintWriter writer, BlockCipherService blockCipherService, AsymmetricCryptographyService asymmetricCryptographyService, BitcoinNodeAPIService bitcoinNodeAPIService) {
        this.writer = writer;
        this.blockCipherService = blockCipherService;
        this.asymmetricCryptographyService = asymmetricCryptographyService;
        this.bitcoinNodeAPIService = bitcoinNodeAPIService;
    }


    @Subscribe
    public void handleContainerInitializationEvent(InitializeContainerEvent event) {
        walletContainer = new LinkedListSecureContainer<>(blockCipherService, event.getPassphrase());
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(event.getFile()));
             ObjectInput objectInput = new ObjectInputStream(bufferedInputStream))
        {
            walletContainer.readExternal(objectInput);
        }
        catch (Exception e) {
            writer.println("ERROR: " + e.getMessage());
            writer.flush();
            System.exit(1);
        }
    }

    @Subscribe
    public void handleSaveContainerEvent(SaveContainerEvent event) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(event.getFile()));
             ObjectOutput objectOutput = new ObjectOutputStream(bufferedOutputStream))
        {
            walletContainer.writeExternal(objectOutput);
        }
        catch (Exception e) {
            writer.println(Colored.ANSI_RED + "ERROR: " + e.getMessage() + Colored.ANSI_RESET);
            writer.flush();
            System.exit(1);
        }
    }

    @Subscribe
    public void handleBalanceEvent(BalanceEvent event) {
        Set<Integer> walletIds = event.getWalletIds();
        if (!checkWalletIdBounds(walletIds))
            return;
        try {
            for (Integer indeks : walletIds) {
                List<Transaction> transactions = bitcoinNodeAPIService.getTransactions(TransactionQueryType.RECEIVED, walletContainer.get(indeks - 1));
                BigDecimal sum = calculateWalletBalance(transactions,
                        Encoding.defaultPubKeyEncoding(walletContainer.get(indeks - 1).getPublic().getEncoded()));
                if (event.isColoredResponse()) {
                    writer.println(indeks + ". " + Colored.ANSI_GREEN + sum + Colored.ANSI_RESET + " BTC");
                } else {
                    writer.println(indeks + ". " + sum + " BTC");
                }
                writer.flush();
            }
        } catch (ExternalNodeInvalidHTTPCodeException e) {
            writer.println("Endpoint returned invalid status code: " + e.getHttpCode() + " while querying transactions");
        }
    }

    private boolean checkWalletIdBounds(Set<Integer> walletIds){
        for (Integer indeks : walletIds) {
            if (indeks - 1 >= walletContainer.size()) {
                writer.println("Invalid wallet ID " + indeks);
                writer.flush();
                return false;
            }
        }
        return true;
    }

    private BigDecimal calculateWalletBalance(List<Transaction> transactions, String walletPublicKey) {
        Set<TransactionOutput> inputs = new HashSet<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            for (TransactionOutput input : transaction.getInputs()) {
                if (input.getReceiverPublicKey().equals(walletPublicKey))
                    inputs.add(input);
            }
        }
        for (Transaction transaction : transactions) {
            for (TransactionOutput output : transaction.getOutputs()) {
                if (output.getReceiverPublicKey().equals(walletPublicKey) && !inputs.contains(output))
                    sum = sum.add(output.getAmount());
            }
        }
        return sum;
    }

    @Subscribe
    public void handleDisplayWalletAddressesEvent(DisplayWalletAddressesEvent event) {
        Set<Integer> walletIds = event.getWalletIds();
        if (!checkWalletIdBounds(walletIds))
            return;
        for (Integer walletId : walletIds) {
            if (event.isColoredResponse()) {
                writer.println(walletId + ". " + Colored.ANSI_YELLOW
                        + Encoding.defaultPubKeyEncoding(walletContainer.get(walletId).getPublic().getEncoded()) + Colored.ANSI_RESET);
            } else {writer.println(walletId + ". " +
                    Encoding.defaultPubKeyEncoding(walletContainer.get(walletId).getPublic().getEncoded()));
            }
            writer.flush();
        }
    }

    @Subscribe
    public void handleNewWalletAddressEvent(NewWalletAddressEvent event) {
        walletContainer = new LinkedListSecureContainer<>(blockCipherService, event.getPassphrase());
        KeyPair newKeyPair = asymmetricCryptographyService.generateNewKeypair();
        if (event.isColoredOutput()) {
            writer.println("Generated a new wallet with address:\n" +
                                Colored.ANSI_YELLOW +
                                Encoding.defaultPubKeyEncoding(newKeyPair.getPublic().getEncoded()) +
                                Colored.ANSI_RESET);
            writer.flush();
        }
        else {
            writer.println("Generate a new wallet with address:\n" +
                               Encoding.defaultPubKeyEncoding(newKeyPair.getPublic().getEncoded()));
            writer.flush();
        }

        walletContainer.add(newKeyPair);
    }

    @Subscribe
    public void handleSendTokensEvent(SendTokensEvent event) {
        if (event.getFrom()>= walletContainer.size()) {
            writer.println("Invalid wallet Id: " + event.getFrom());
            writer.flush();
            return;
        }
        String address = Encoding.defaultPubKeyEncoding(walletContainer.get(event.getFrom()-1).getPublic().getEncoded());
        if (event.doubleCheck(address)) {
            // TODO: Implement token sending to another public key address logic
        }
    }

    @Subscribe
    public void handleTransactionsEvent(TransactionsEvent event) {
        // TODO: Implement transactions output
        Set<Integer> walletIds = event.getWalletIds();
        if (!checkWalletIdBounds(walletIds))
            return;
        try {
            for (Integer indeks : walletIds) {
                String address = Encoding.defaultPubKeyEncoding(walletContainer.get(indeks - 1).getPublic().getEncoded());
                writer.println(indeks + ". " + address);
                writer.flush();
                List<Transaction> transactions = bitcoinNodeAPIService.getTransactions(event.getTransactionQueryType(), walletContainer.get(indeks - 1));
                for (Transaction transaction : transactions) {
                    TransactionOutput output;
                    if (transaction.getSenderPublicKey().equals(address) &&
                            (output = findExternalRecipiantTransactionOutput(address, transaction)) != null) {
                        writer.println("- " +
                                address.substring(0, 5) +
                                "..." +
                                address.substring(address.length()-5) +
                                " -> " +
                                output.getReceiverPublicKey().substring(0, 5) +
                                "..." +
                                output.getReceiverPublicKey().substring(output.getReceiverPublicKey().length()-5) +
                                " " +
                                output.getAmount() +
                                " BTC"
                                );
                    } else if (!transaction.getSenderPublicKey().equals(address) &&
                            (output = findWalletAddressRecipiantTransactionOutput(address, transaction)) != null) {
                        writer.println("- " +
                                transaction.getSenderPublicKey().substring(0, 5) +
                                "..." +
                                transaction.getSenderPublicKey().substring(transaction.getSenderPublicKey().length()-5) +
                                " -> " +
                                address.substring(0, 5) +
                                "..." +
                                address.substring(address.length()-5) +
                                " " +
                                output.getAmount() +
                                " BTC"
                        );
                    }
                    writer.flush();
                }
            }
        } catch (ExternalNodeInvalidHTTPCodeException e) {
            writer.println("Invalid http code: " + e.getHttpCode());
            writer.flush();
        }
        switch (event.getTransactionQueryType()) {
            case ALL: break;
            case SENT: break;
            case RECEIVED: break;
        }
    }
    private TransactionOutput findExternalRecipiantTransactionOutput(String walletAddress, Transaction transaction) {
        for (TransactionOutput output : transaction.getOutputs()) {
            if (!output.getReceiverPublicKey().equals(walletAddress)) {
                return output;
            }
        }
        return null;
    }
    private TransactionOutput findWalletAddressRecipiantTransactionOutput(String walletAddress, Transaction transaction) {
        for (TransactionOutput output : transaction.getOutputs()) {
            if (output.getReceiverPublicKey().equals(walletAddress)) {
                return output;
            }
        }
        return null;
    }

}
