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

    public void mineBlock() {
        Block block = createBlock(); // The call of a blocking thread which creates a new block
        synchronized (minerManager) { // The first miner who created a new block locks this code part
            if (newBlockGenerated) { // If a miner is late they send a transaction, otherwise add the new block
                Optional<Transaction> transaction = createTransaction();
                transaction.ifPresent(minerManager::performTransaction);
            } else {
                minerManager.addBlock(block, minerId);
            }
        }
        newBlockGenerated = false;
    }

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

    public Block createBlock() {
        NewBlockInfo newBlockInfo = minerManager.getNewBlockInfo();
        return new Block(newBlockInfo.getBlockId(), newBlockInfo.getHashPrev(), newBlockInfo.getPrefix(), minerId);
    }

    public void update() {
        newBlockGenerated = true;
    }

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
