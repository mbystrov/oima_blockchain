package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.Block;
import com.griddynamics.blockchain.Blockchain;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

public class MinerManagerTest {

    @Test
    public void addMiner() {
        TransactionManager transactionManager = Mockito.mock(TransactionManager.class);
        BlockchainManager blockchainManager = Mockito.mock(BlockchainManager.class);
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);

        Assert.assertEquals(0, minerManager.getMinersList().size());
        new Miner(1, minerManager);
        Assert.assertEquals(1, minerManager.getMinersList().size());
    }

    @Test
    public void addBlock() {
        TransactionManager transactionManager = Mockito.mock(TransactionManager.class);
        BlockchainManager blockchainManager = Mockito.mock(BlockchainManager.class);
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);
        Block block = Mockito.mock(Block.class);
        minerManager.addBlock(block, 1);

        Mockito.verify(block, times(1)).setMessage(Mockito.any());
        Mockito.verify(transactionManager, times(2)).getMessages();
        Mockito.verify(transactionManager, times(1)).updateWallet(1, 100);
    }

    @Test
    public void isNewBlockAllowed() {
        TransactionManager transactionManager = Mockito.mock(TransactionManager.class);
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(1, blockchain);
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);
        Block block = new Block(1, "0", "0", 1);

        Assert.assertTrue(blockchainManager.isNewBlockAllowed());
        minerManager.addBlock(block, 1);
        Assert.assertFalse(blockchainManager.isNewBlockAllowed());
    }
}