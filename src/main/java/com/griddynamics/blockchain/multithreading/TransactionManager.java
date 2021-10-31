package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.crypt.Transaction;
import com.griddynamics.blockchain.utils.BlockManagerUtils;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionManager {
    final ConcurrentHashMap<Integer, PublicKey> publicKeys = new ConcurrentHashMap<>();
    final ConcurrentHashMap<Integer, Integer> transactionOrder = new ConcurrentHashMap<>();
    final ConcurrentHashMap<Integer, Integer> wallet = new ConcurrentHashMap<>();
    final List<String> messages = new ArrayList<>();

    /**
     * Initialize miner:
     * - adds its public key to the map,
     * - sets initial value of transactionOrder to zero,
     * - sets the initial VC amount to 100
     *
     * @param minerId
     * @param publicKey
     */
    public void initializeMiner(int minerId, PublicKey publicKey) {
        publicKeys.put(minerId, publicKey);
        transactionOrder.put(minerId, 0);
        wallet.put(minerId, 100);
    }

    /**
     * Checks correctness of a received transaction by comparing a publicKey and transaction order.
     * In case of validity update wallet, increase transaction order and save a transactionMessage.
     *
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

    /**
     * Performs transaction operation from senderMinerId to receiverMinerId
     *
     * @param senderMinerId
     * @param receiverMinerId
     * @param amount
     */
    public void updateWallet(int senderMinerId, int receiverMinerId, int amount) {
        if (wallet.get(receiverMinerId) != null && wallet.get(senderMinerId) != null) {
            wallet.put(senderMinerId, wallet.get(senderMinerId) - amount);
            wallet.put(receiverMinerId, wallet.get(receiverMinerId) + amount);
        }
    }

    /**
     * Adds VC to minerId's wallet
     *
     * @param minerId id of a miner whose wallet will be updated
     * @param amount
     */
    public void updateWallet(int minerId, int amount) {
        wallet.put(minerId, wallet.get(minerId) + amount);
    }

    public List<String> getMessages() {
        return messages;
    }
}
