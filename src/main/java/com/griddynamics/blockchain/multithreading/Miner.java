package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.Block;
import com.griddynamics.blockchain.crypt.KeyPairCreator;
import com.griddynamics.blockchain.crypt.Transaction;
import com.griddynamics.blockchain.crypt.TransactionBuilder;

import java.net.Inet4Address;
import java.security.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Miner implements Runnable {
    private boolean newBlockGenerated = false;
    private final int minerId;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private int transactionId = 0;
    private final MinerManager minerManager;

    public Miner(int minerId, MinerManager minerManager) {
        this.minerId = minerId;
        this.minerManager = minerManager;
        try {
            KeyPair keyPair = KeyPairCreator.createKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        minerManager.addMiner(this);
    }

    /**
     * Creates a block. Adds the block to a blockchain.
     * When a thread adds block, @{newBlockGenerated} is set to true.
     * If the block has already been created by another thread, performs transaction.
     */
    public void mineBlock() {
        Block block = createBlock();
        synchronized (minerManager) {
            if (newBlockGenerated) {
                Optional<Transaction> transaction = createTransaction();
                transaction.ifPresent(minerManager::performTransaction);
            } else {
                minerManager.addBlock(block, minerId);
            }
        }
        newBlockGenerated = false;
    }

    /**
     * @return an Optional<Transaction> for a random miner with random amount of VC.
     */
    public Optional<Transaction> createTransaction() {
        int amount = new Random().nextInt(100);
        List<Integer> minerIdList = minerManager.getMinersList()
                .stream()
                .map(Miner::getMinerId)
                .filter(x -> x != minerId)
                .collect(Collectors.toList());
        if (!minerIdList.isEmpty()) {
            int receiverMinerId = minerIdList.get(new Random().nextInt(minerIdList.size()));
            Transaction transaction = TransactionBuilder.build(transactionId++, minerId, receiverMinerId, amount, privateKey);
            return Optional.of(transaction);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Passes information about the last block to arguments of Block's creation
     *
     * @return a new Block object
     */
    public Block createBlock() {
        NewBlockInfo newBlockInfo = minerManager.getNewBlockInfo();
        return new Block(newBlockInfo.getBlockId(), newBlockInfo.getHashPrev(), newBlockInfo.getPrefix(), minerId);
    }

    /**
     * A variable informing a user that block was created by another thread is set to true
     */
    public void update() {
        newBlockGenerated = true;
    }

    /**
     * Sends command to mine a new block if a minerManager allows a client to do that
     */
    @Override
    public void run() {
        while (minerManager.isNewBlockAllowed()) {
            mineBlock();
        }
    }

    public int getMinerId() {
        return minerId;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
