package org.students.simplebitcoinwallet.ui.event.listener;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.students.simplebitcoinwallet.data.Transaction;
import org.students.simplebitcoinwallet.data.TransactionOutput;
import org.students.simplebitcoinwallet.exception.ExternalNodeInvalidHTTPCodeException;
import org.students.simplebitcoinwallet.service.AsymmetricCryptographyService;
import org.students.simplebitcoinwallet.service.BitcoinNodeAPIService;
import org.students.simplebitcoinwallet.ui.event.*;
import org.students.simplebitcoinwallet.util.Colored;
import org.students.simplebitcoinwallet.util.Encoding;
import org.students.simplebitcoinwallet.util.SecureContainer;

import java.io.*;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Wallet UI event listeners which handle
 */
public class WalletEventListener {

    // injected dependencies
    private final AsymmetricCryptographyService asymmetricCryptographyService;
    private final PrintWriter writer;
    private final BitcoinNodeAPIService bitcoinNodeAPIService;
    private final SecureContainer<KeyPair> walletContainer;
    private final DecimalFormat df = new DecimalFormat("0.000000");

    @Inject
    public WalletEventListener(PrintWriter writer,
                               AsymmetricCryptographyService asymmetricCryptographyService,
                               BitcoinNodeAPIService bitcoinNodeAPIService,
                               SecureContainer<KeyPair> walletContainer) {
        this.writer = writer;
        this.asymmetricCryptographyService = asymmetricCryptographyService;
        this.bitcoinNodeAPIService = bitcoinNodeAPIService;
        this.walletContainer = walletContainer;
    }


    @Subscribe
    public void handleContainerInitializationEvent(InitializeContainerEvent event) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(event.getFile()));
             ObjectInput objectInput = new ObjectInputStream(bufferedInputStream))
        {
            walletContainer.setPassphrase(event.getPassphrase());
            walletContainer.setFile(event.getFile());
            walletContainer.readExternal(objectInput);
        }
        catch (Exception e) {
            writer.println("ERROR: " + e.getMessage());
            writer.flush();
            System.exit(1);
        }
    }

    @Subscribe
    public void handleWalletFileCreationEvent(WalletFileCreationEvent event) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(event.getFile()));
             ObjectOutput objectOutput = new ObjectOutputStream(bufferedOutputStream))
        {
            walletContainer.setFile(event.getFile());
            walletContainer.setPassphrase(event.getPassphrase());
            walletContainer.writeExternal(objectOutput);
        }
        catch (Exception e) {
            writer.println("ERROR: " + e.getMessage());
            writer.flush();
            System.exit(1);
        }
    }

    @Subscribe
    public void handleSaveContainerEvent(SaveContainerEvent event) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(walletContainer.getFile()));
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
        Set<Integer> walletIds = getWalletIds(event.getWalletIds());
        if (!checkWalletIdBounds(walletIds))
            return;
        try {
            for (Integer index : walletIds) {
                List<TransactionOutput> unspentTransactionOutputs = bitcoinNodeAPIService.getUnspentTransactionOutputs(walletContainer.get(index - 1).getPublic());
                BigDecimal sum = unspentTransactionOutputs.stream().map(TransactionOutput::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (event.isColoredResponse()) {
                    writer.println(index + ". " + Colored.ANSI_GREEN + df.format(sum) + Colored.ANSI_RESET + " BTC");
                } else {
                    writer.println(index + ". " + df.format(sum) + " BTC");
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

    @Subscribe
    public void handleDisplayWalletAddressesEvent(DisplayWalletAddressesEvent event) {
        Set<Integer> walletIds = getWalletIds(event.getWalletIds());
        if (!checkWalletIdBounds(walletIds))
            return;

        for (Integer walletId : walletIds) {
            if (event.isColoredResponse()) {
                writer.println(walletId + ". " + Colored.ANSI_YELLOW
                        + Encoding.defaultPubKeyEncoding(walletContainer.get(walletId - 1).getPublic().getEncoded()) + Colored.ANSI_RESET);
            } else {writer.println(walletId + ". " +
                    Encoding.defaultPubKeyEncoding(walletContainer.get(walletId - 1).getPublic().getEncoded()));
            }
            writer.flush();
        }
    }

    private Set<Integer> getWalletIds(Set<Integer> payload) {
        if (!payload.isEmpty())
            return payload;

        Set<Integer> walletIds = new HashSet<>();
        for (int i = 0; i < walletContainer.size(); i++)
            walletIds.add(i + 1);

        return walletIds;
    }

    @Subscribe
    public void handleNewWalletAddressEvent(NewWalletAddressEvent event) {
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
        if (event.getFrom() - 1 >= walletContainer.size()) {
            writer.println("Invalid wallet Id: " + event.getFrom());
            writer.flush();
            return;
        }

        KeyPair fromAddressKeyPair = walletContainer.get(event.getFrom() - 1);
        String fromAddress = Encoding.defaultPubKeyEncoding(fromAddressKeyPair.getPublic().getEncoded());
        if (event.doubleCheck(fromAddress)) {
            try {
                List<TransactionOutput> utxos = bitcoinNodeAPIService.getUnspentTransactionOutputs(fromAddressKeyPair.getPublic());
                List<TransactionOutput> spendableUTXOs = findMinimalAmountOfUTXOsForTransactions(utxos, event.getAmount());
                Transaction transaction = Transaction.builder()
                        .inputs(spendableUTXOs)
                        .outputs(List.of(TransactionOutput.builder().amount(event.getAmount()).receiverPublicKey(event.getRecipientAddress()).build()))
                        .senderPublicKey(fromAddress)
                        .timestamp(bitcoinNodeAPIService.serverTime())
                        .build();

                // check if some tokens need to be refunded
                BigDecimal transferSum = spendableUTXOs.stream().map(TransactionOutput::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (transferSum.compareTo(event.getAmount()) > 0) {
                    BigDecimal refundAmount = transferSum.subtract(event.getAmount());
                    transaction.getOutputs().add(TransactionOutput.builder()
                            .amount(refundAmount)
                            .receiverPublicKey(fromAddress)
                            .build());
                }

                transaction.setTransactionHash(Encoding.toHexString(asymmetricCryptographyService.digestObject(transaction)));

                // sign every transaction
                for (TransactionOutput output : transaction.getOutputs()) {
                    final byte[] hash = Encoding.hexStringToBytes(transaction.getTransactionHash());
                    final byte[] receiverPublicKey = Encoding.defaultPubKeyDecoding(output.getReceiverPublicKey());
                    final byte[] signatureMessage = Arrays.copyOf(hash, hash.length + receiverPublicKey.length);
                    System.arraycopy(receiverPublicKey, 0, signatureMessage, hash.length, receiverPublicKey.length);

                    // sign the transfer
                    output.setSignature(Encoding.toHexString(asymmetricCryptographyService.signMessage(signatureMessage, fromAddressKeyPair.getPrivate().getEncoded())));
                }

                // broadcast the transaction
                bitcoinNodeAPIService.brodcastTransaction(transaction);
            }
            catch (Exception e) {
                writer.println(Colored.ANSI_RED + "ERROR: " + e.getMessage() + Colored.ANSI_RESET);
                writer.flush();
            }
        }
    }

    private List<TransactionOutput> findMinimalAmountOfUTXOsForTransactions(List<TransactionOutput> utxos, BigDecimal amountToSend) {
        utxos.sort(Comparator.comparing(TransactionOutput::getAmount));

        List<TransactionOutput> utxoInputs = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (TransactionOutput utxo : utxos) {
            sum = sum.add(utxo.getAmount());
            utxoInputs.add(utxo);
            if (sum.compareTo(amountToSend) >= 0)
                break;
        }

        return utxoInputs;
    }

    @Subscribe
    public void handleTransactionsEvent(TransactionsEvent event) {
        Set<Integer> walletIds = getWalletIds(event.getWalletIds());
        if (!checkWalletIdBounds(walletIds))
            return;

        try {
            for (Integer index : walletIds) {
                String address = Encoding.defaultPubKeyEncoding(walletContainer.get(index - 1).getPublic().getEncoded());
                writer.println(index + ". " + address);
                writer.flush();
                List<Transaction> transactions = bitcoinNodeAPIService.getTransactions(event.getTransactionQueryType(), walletContainer.get(index - 1).getPublic());

                if (transactions.isEmpty()) {
                    writer.println("  (no transactions)");
                    writer.flush();
                }

                for (Transaction transaction : transactions) {
                    TransactionOutput output;
                    if (transaction.getSenderPublicKey().equals(address) &&
                            (output = findExternalRecipiantTransactionOutput(address, transaction)) != null) {
                        writer.println((event.isColoredResponse() ? Colored.ANSI_RED : "") + "- " +
                                address.substring(0, 5) +
                                "..." +
                                address.substring(address.length()-5) +
                                " -> " +
                                output.getReceiverPublicKey().substring(0, 5) +
                                "..." +
                                output.getReceiverPublicKey().substring(output.getReceiverPublicKey().length()-5) +
                                " " +
                                df.format(output.getAmount()) +
                                " BTC" + (event.isColoredResponse() ? Colored.ANSI_RESET : ""));
                    } else if (!transaction.getSenderPublicKey().equals(address) &&
                            (output = findWalletAddressRecipiantTransactionOutput(address, transaction)) != null) {
                        writer.println((event.isColoredResponse() ? Colored.ANSI_GREEN : "") + "- " +
                                transaction.getSenderPublicKey().substring(0, 5) +
                                "..." +
                                transaction.getSenderPublicKey().substring(transaction.getSenderPublicKey().length()-5) +
                                " -> " +
                                address.substring(0, 5) +
                                "..." +
                                address.substring(address.length()-5) +
                                " " +
                                df.format(output.getAmount()) +
                                " BTC" + (event.isColoredResponse() ? Colored.ANSI_RESET : ""));
                    }
                    writer.flush();
                }
            }
        } catch (ExternalNodeInvalidHTTPCodeException e) {
            writer.println("Invalid http code: " + e.getHttpCode());
            writer.flush();
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
