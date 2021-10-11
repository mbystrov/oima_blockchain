package com.gd.blockchain.multithreading;

import com.gd.blockchain.Block;
import com.gd.blockchain.Blockchain;

import java.util.ArrayList;
import java.util.List;

public class BlockManager {
    private int blockNumber;
    private final int maxBlockchainSize;
    private boolean continues = true;
    private final MinerManager minerManager;
    private final Blockchain blockchain;
    private final List<String> messages = new ArrayList<>();

    public BlockManager(MinerManager minerManager, int maxSize, Blockchain blockchain) {
        this.maxBlockchainSize = maxSize;
        this.minerManager = minerManager;
        this.blockchain = blockchain;
    }

    public void addBlock(Block block) {
        block.setMessage(messages);
        blockchain.addBlock(block);
        messages.clear();
        blockNumber++;
        if (blockNumber >= maxBlockchainSize) {
            continues = false;
        }
        minerManager.notifyAllMiners();
    }

    public boolean isContinues() {
        return continues;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void sendMessage(String message) {
        messages.add(message);
    }
}
