package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.Block;
import com.griddynamics.blockchain.Blockchain;
import com.griddynamics.blockchain.crypt.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class MinerTest {

    @Test
    public void checkMineBlockIncreasesBlockchainSize() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(2, blockchain);
        TransactionManager transactionManager = new TransactionManager();
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);
        Miner miner = new Miner(1, minerManager);

        Assert.assertEquals(0, blockchainManager.getCreatedBlocksNumber());
        miner.mineBlock();
        Assert.assertEquals(1, blockchainManager.getCreatedBlocksNumber());
    }

    @Test
    public void checkMineBlockDoesnotAddBlockIfNewBlockGeneratedIsTrue() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(2, blockchain);
        TransactionManager transactionManager = new TransactionManager();
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);
        Miner miner = new Miner(1, minerManager);

        miner.update();
        Assert.assertEquals(0, blockchainManager.getCreatedBlocksNumber());
        miner.mineBlock();
        Assert.assertEquals(0, blockchainManager.getCreatedBlocksNumber());
    }

    @Test
    public void createEmptyTransaction() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(2, blockchain);
        TransactionManager transactionManager = new TransactionManager();
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);
        Miner miner = new Miner(1, minerManager);
        Optional<Transaction> transaction = miner.createTransaction();

        Assert.assertTrue(transaction.isEmpty());
    }

    @Test
    public void createNonEmptyTransaction() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(2, blockchain);
        TransactionManager transactionManager = new TransactionManager();
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);
        Miner miner = new Miner(1, minerManager);
        Miner anotherMiner = new Miner(2, minerManager);
        Optional<Transaction> transaction = miner.createTransaction();

        Assert.assertTrue(transaction.isPresent());
        Assert.assertEquals(anotherMiner.getMinerId(), transaction.get().getReceiverMinerId());
    }

    @Test
    public void createBlock() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(2, blockchain);
        TransactionManager transactionManager = new TransactionManager();
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);
        Miner miner = new Miner(1, minerManager);
        Block block = miner.createBlock();

        Assert.assertEquals(miner.getMinerId(), block.getMinerId());
    }

}