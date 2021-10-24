package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.Block;
import com.griddynamics.blockchain.Blockchain;
import com.griddynamics.blockchain.crypt.KeyPairCreator;
import com.griddynamics.blockchain.crypt.Message;
import com.griddynamics.blockchain.crypt.Transaction;
import com.griddynamics.blockchain.utils.Tuple;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BlockManager {
    private int blockNumber;
    private final int maxBlockchainSize;
    private boolean blocksLeft = true;
    private final MinerManager minerManager;
    private final Blockchain blockchain;
    private final List<String> messages = new ArrayList<>();
    private final ConcurrentHashMap<Integer, Tuple<PublicKey, Integer>> publicKeys = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Integer> wallet = new ConcurrentHashMap<>();

    public BlockManager(MinerManager minerManager, int maxSize, Blockchain blockchain) {
        this.maxBlockchainSize = maxSize;
        this.minerManager = minerManager;
        this.blockchain = blockchain;
    }

    public void addBlock(Block block, int minerId) {
        block.setMessage(messages);
        blockchain.addBlock(block);
        messages.clear();
        blockNumber++;
        if (blockNumber >= maxBlockchainSize) {
            blocksLeft = false;
        }
        wallet.put(minerId, wallet.get(minerId) + 100);
        minerManager.notifyAllMiners();
    }

    public boolean areAllBlocksLeft() {
        return blocksLeft;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void sendMessage(Message message) {
        int identifier = message.getIdentifier();
        byte[] signedMsg = message.getSignedMsg();
        String msgText = message.getText();
        int minerId = message.getMinerId();
        Tuple<PublicKey, Integer> minerIdTuple = publicKeys.get(minerId);
        if (minerIdTuple.l == identifier) {
            try {
                if (KeyPairCreator.verifySignature(msgText.getBytes(), signedMsg, minerIdTuple.k)) {
                    messages.add(msgText);
                    publicKeys.put(minerId, new Tuple<>(minerIdTuple.k, minerIdTuple.l + 1));
                } else {
                    System.out.println(minerId + " Signature is wrong");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(minerId + " sent msg with expired identifier");
        }
    }

    public void initializeMiner(int minerId, PublicKey publicKey) {
//        System.out.println(minerId + " miner id");
        publicKeys.put(minerId, new Tuple<>(publicKey, 0));
        wallet.put(minerId, 100);
    }

    public int getMinersCount() {
        return minerManager.getMiners().size();
    }

    public synchronized void sendTransaction(Transaction transaction) {
        int identifier = transaction.getIdentifier();
        byte[] signedTransaction = transaction.getSignedTransaction();
        int amount = transaction.getAmount();
        int senderMinerId = transaction.getSenderMinerId();
        int receiverMinerId = transaction.getReceiverMinerId();
        String transactionString = transaction.toString();
        Tuple<PublicKey, Integer> minerIdTuple = publicKeys.get(senderMinerId);
        if (minerIdTuple.l == identifier) {
            try {
                if (KeyPairCreator.verifySignature(transactionString.getBytes(), signedTransaction, minerIdTuple.k)) {
                    int amountSenderMiner = wallet.get(senderMinerId);
                    boolean isReceiverMinerWalletExist = wallet.containsKey(receiverMinerId);
                    if (amountSenderMiner >= amount && isReceiverMinerWalletExist) {
                        wallet.put(senderMinerId, wallet.get(senderMinerId) - amount);
                        wallet.put(receiverMinerId, wallet.get(receiverMinerId) + amount);
                        messages.add(transactionString);
                    }
                    publicKeys.put(senderMinerId, new Tuple<>(minerIdTuple.k, minerIdTuple.l + 1));
                } else {
                    System.out.println(senderMinerId + " Signature is wrong");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(senderMinerId + " sent msg with expired identifier");
        }
    }
}
