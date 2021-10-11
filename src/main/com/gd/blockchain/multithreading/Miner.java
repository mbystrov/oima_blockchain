package com.gd.blockchain.multithreading;

import com.gd.blockchain.Block;
import com.gd.blockchain.Blockchain;
import com.gd.blockchain.Messages;

public class Miner implements Runnable {
    private boolean blockGenerated = false;
    private final int minerId;
    private final BlockManager blockManager;
    private final Blockchain blockchain;

    public Miner(int minerId, MinerManager minerManager, BlockManager blockManager, Blockchain blockchain) {
        this.minerId = minerId;
        minerManager.addMiner(this);
        this.blockManager = blockManager;
        this.blockchain = blockchain;
    }

    public void mineBlock() {
        while (blockManager.isContinues()) {
//            int blockNumber = blockManager.getBlockNumber();
//            System.out.printf("Miner %d is working on block %d\n", minerId, blockNumber);
            String hashPrev = blockchain.getHashOneBeforeLast();
            String prefix = new String(new char[blockchain.getZerosNumber()]).replace('\0', '0');
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (blockManager) {
                if (blockGenerated) {
//                    System.out.printf("Miner %d is late to create %d block\n", minerId, blockNumber);
                    blockManager.sendMessage("Miner №" + minerId + ": " + Messages.getRandomMessage());
                } else {
//                    System.out.printf("\n\nMiner %d generated block %d\n", minerId, blockNumber);
                    blockManager.addBlock(new Block(blockchain.generateNewId(), hashPrev, prefix, minerId));
                }
            }
            blockGenerated = false;
        }
    }

    public void update() {
        blockGenerated = true;
    }

    @Override
    public void run() {
        mineBlock();
    }
}
