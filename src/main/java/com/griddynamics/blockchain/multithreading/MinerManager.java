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

    /**
     * Initializes miner, adds miner to the minersList
     *
     * @param miner
     */
    public void addMiner(Miner miner) {
        transactionManager.initializeMiner(miner.getMinerId(), miner.getPublicKey());
        minersList.add(miner);
    }

    /**
     * Adds messages to a block. Adds the block to a blockchain. Clears transaction messages.
     * Increases amount of VC of a miner who added a block to 100.
     * Notifies all threads about a successfully generated block.
     *
     * @param block
     * @param minerId
     */
    public void addBlock(Block block, int minerId) {
        block.setMessage(transactionManager.getMessages());
        blockchainManager.addBlock(block);
        transactionManager.getMessages().clear();
        transactionManager.updateWallet(minerId, 100);
        notifyAllMiners();
    }

    /**
     * @return boolean saying whether a new block can be added to a blockchain
     */
    public boolean isNewBlockAllowed() {
        return blockchainManager.isNewBlockAllowed();
    }

    public void performTransaction(Transaction transaction) {
        transactionManager.performTransaction(transaction);
    }

    /**
     * Informs miner threads about successful block generation by one of threads.
     */
    public void notifyAllMiners() {
        minersList.forEach(Miner::update);
    }

    public int getMinersCount() {
        return minersList.size();
    }

    /**
     * @return information about the last block in a blockchain
     */
    public NewBlockInfo getNewBlockInfo() {
        return blockchainManager.getNewBlockInfo();
    }

    public List<Miner> getMinersList() {
        return minersList;
    }
}
