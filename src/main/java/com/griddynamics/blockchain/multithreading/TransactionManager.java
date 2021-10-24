package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.crypt.Transaction;
import com.griddynamics.blockchain.utils.BlockManagerUtils;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionManager {
    private final ConcurrentHashMap<Integer, PublicKey> publicKeys = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Integer> transactionOrder = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Integer> wallet = new ConcurrentHashMap<>();
    private final List<String> messages = new ArrayList<>();

    public void initializeMiner(int minerId, PublicKey publicKey) {
        publicKeys.put(minerId, publicKey);
        transactionOrder.put(minerId, 0);
        wallet.put(minerId, 100);
    }

    /**
     * Checks correctness of a received transaction by comparing a publicKey and transaction order.
     * In case of validity update wallet, increase transaction order and save a transactionMessage.
     * @param transaction transaction received from Miner
     */
    public void performTransaction(Transaction transaction) {
        int amount = transaction.getAmount();
        int senderMinerId = transaction.getSenderMinerId();
        int receiverMinerId = transaction.getReceiverMinerId();
        String transactionMessage = transaction.toString();

        if (BlockManagerUtils.verifyTransaction(transaction, publicKeys.get(senderMinerId), transactionOrder.get(senderMinerId))) {
            int amountSenderMiner = wallet.get(senderMinerId);
            boolean isReceiverMinerWalletExist = wallet.containsKey(receiverMinerId);
            if (amountSenderMiner >= amount && isReceiverMinerWalletExist) {
                updateWallet(senderMinerId, receiverMinerId, amount);
                messages.add(transactionMessage);
            }
            transactionOrder.put(senderMinerId, transactionOrder.get(senderMinerId) + 1);
        }
    }

    public void updateWallet(int senderMinerId, int receiverMinerId, int amount) {
        wallet.put(senderMinerId, wallet.get(senderMinerId) - amount);
        wallet.put(receiverMinerId, wallet.get(receiverMinerId) + amount);
    }

    public void updateWallet(int minerId, int amount) {
        wallet.put(minerId, wallet.get(minerId) + amount);
    }

    public List<String> getMessages() {
        return messages;
    }
}
