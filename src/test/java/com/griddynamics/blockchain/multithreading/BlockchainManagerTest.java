package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.Block;
import com.griddynamics.blockchain.Blockchain;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BlockchainManagerTest {

    @Test
    public void constructorThrowsExceptionWhenMaxSizeLessThanOne() {
        Blockchain blockchain = new Blockchain(0);
        Assert.assertThrows(IllegalArgumentException.class, () -> new BlockchainManager(0, blockchain));
    }

    @Test
    public void addBlock1() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(1, blockchain);
        Block block = new Block(1, "0", "0", 1);

        Assert.assertEquals(0, blockchainManager.getCreatedBlocksNumber());
        Assert.assertTrue(blockchainManager.isNewBlockAllowed());
        blockchainManager.addBlock(block);
        Assert.assertEquals(1, blockchainManager.getCreatedBlocksNumber());
        Assert.assertFalse(blockchainManager.isNewBlockAllowed());
    }


    @Test
    public void addBlock2() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(2, blockchain);
        Block block = new Block(1, "0", "0", 1);

        Assert.assertEquals(0, blockchainManager.getCreatedBlocksNumber());
        Assert.assertTrue(blockchainManager.isNewBlockAllowed());
        blockchainManager.addBlock(block);
        Assert.assertEquals(1, blockchainManager.getCreatedBlocksNumber());
        Assert.assertTrue(blockchainManager.isNewBlockAllowed());
    }

    @Test
    public void isNewBlockAllowed() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(1, blockchain);
        Assert.assertTrue(blockchainManager.isNewBlockAllowed());
    }

    @Test
    public void getNewBlockInfo() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(2, blockchain);
        Block block = new Block(1, "0", "0", 1);

        blockchainManager.addBlock(block);
        NewBlockInfo newBlockInfo = blockchainManager.getNewBlockInfo();

        Assert.assertEquals(2, newBlockInfo.getBlockId());
        Assert.assertEquals(block.getHashCurr(), newBlockInfo.getHashPrev());
    }
}