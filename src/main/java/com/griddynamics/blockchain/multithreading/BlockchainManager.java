package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.Block;
import com.griddynamics.blockchain.Blockchain;

import java.util.List;

/**
 * This service is in charge of interaction between a blockchain and a miner manager
 */
public class BlockchainManager {
    private final int maxBlockchainSize;
    private boolean emptySpace = true;
    private final Blockchain blockchain;

    /**
     * @param maxSize maximum size of a blockchain. Should be greater then 0
     * @param blockchain blockchain
     */
    public BlockchainManager(int maxSize, Blockchain blockchain) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("Max size should be greater than 1");
        }
        this.maxBlockchainSize = maxSize;
        this.blockchain = blockchain;
    }

    /**
     * Adds a block to a blockchain, increases number of blocks in a blockchainManager.
     * If the number of blocks is greater or equal to maximum blockchain size, sets boolean emptySpace to false.
     * @param block block to be added to a blockchain
     */
    public void addBlock(Block block) {
        blockchain.addBlock(block);
        if (blockchain.getBlockchainSize() >= maxBlockchainSize) {
            emptySpace = false;
        }
    }

    /**
     * Returns boolean status saying whether a new block allowed
     * @return boolean emptySpace
     */
    public boolean isNewBlockAllowed() {
        return emptySpace;
    }

    /**
     * Gets info of a next block
     * @return new block info
     */
    public NewBlockInfo getNewBlockInfo() {
        return new NewBlockInfo(blockchain.generateNewId(),
                blockchain.getHashOneBeforeLast(),
                new String(new char[blockchain.getZerosNumber()]).replace('\0', '0'));
    }

    /**
     *
     * @return number of created blocks
     */
    public int getCreatedBlocksNumber() {
        return blockchain.getBlockchainSize();
    }

}
