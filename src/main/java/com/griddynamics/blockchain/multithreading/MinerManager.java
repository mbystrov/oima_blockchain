package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.Block;
import com.griddynamics.blockchain.crypt.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MinerManager {
    private final List<Miner> minersList = new ArrayList<>();

    private final TransactionManager transactionManager;
    private final BlockchainManager blockchainManager;
    public MinerManager(TransactionManager transactionManager, BlockchainManager blockchainManager) {
        this.transactionManager = transactionManager;
        this.blockchainManager = blockchainManager;
    }

    public void addMiner(Miner miner) {
        transactionManager.initializeMiner(miner.getMinerId(), miner.getPublicKey());
        minersList.add(miner);
    }

    public void addBlock(Block block, int minerId) {
        block.setMessage(transactionManager.getMessages());
        blockchainManager.addBlock(block);
        transactionManager.getMessages().clear();
        transactionManager.updateWallet(minerId, 100);
        notifyAllMiners();
    }

    public boolean isNewBlockAllowed() {
        return blockchainManager.isNewBlockAllowed();
    }

    public void performTransaction(Transaction transaction) {
        transactionManager.performTransaction(transaction);
    }

    public void notifyAllMiners() {
        minersList.forEach(Miner::update);
    }

    public int getMinersCount() {
        return minersList.size();
    }

    public NewBlockInfo getNewBlockInfo() {
        return blockchainManager.getNewBlockInfo();
    }

    public List<Miner> getMinersList() {
        return minersList;
    }
}
