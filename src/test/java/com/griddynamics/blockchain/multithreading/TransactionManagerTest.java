package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.Blockchain;
import com.griddynamics.blockchain.crypt.KeyPairCreator;
import com.griddynamics.blockchain.crypt.Transaction;
import com.griddynamics.blockchain.crypt.TransactionBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.Optional;

import static org.junit.Assert.*;

public class TransactionManagerTest {

    @Test
    public void initializeMiner() {
        TransactionManager transactionManager = new TransactionManager();

        assertEquals(0, transactionManager.publicKeys.size());
        assertEquals(0, transactionManager.transactionOrder.size());
        assertEquals(0, transactionManager.wallet.size());

        transactionManager.initializeMiner(1, Mockito.mock(PublicKey.class));

        assertEquals(1, transactionManager.publicKeys.size());
        assertEquals(1, transactionManager.transactionOrder.size());
        assertEquals(1, transactionManager.wallet.size());
        assertEquals(100, (int) transactionManager.wallet.get(1));
    }

    @Test
    public void performTransaction() {
        Blockchain blockchain = new Blockchain(0);
        BlockchainManager blockchainManager = new BlockchainManager(5, blockchain);
        TransactionManager transactionManager = new TransactionManager();
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);
        Miner miner1 = new Miner(1, minerManager);
        Miner miner2 = new Miner(2, minerManager);
        Optional<Transaction> transaction = miner1.createTransaction();

        int miner1VC = transactionManager.wallet.get(miner1.getMinerId());
        int miner2VC = transactionManager.wallet.get(miner2.getMinerId());
        assertEquals(0, transactionManager.messages.size());
        assertEquals(0, (int) transactionManager.transactionOrder.get(miner1.getMinerId()));

        assertEquals(miner1VC, 100);
        assertEquals(miner2VC, 100);

        transactionManager.performTransaction(transaction.get());

        assertTrue(transactionManager.wallet.get(miner1.getMinerId()) < 100);
        assertTrue(transactionManager.wallet.get(miner2.getMinerId()) > 100);
        assertEquals(1, transactionManager.messages.size());
        assertEquals(1, (int) transactionManager.transactionOrder.get(miner1.getMinerId()));
    }

    @Test
    public void negativePerformTransaction() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = KeyPairCreator.createKeyPair();
        Transaction transaction = TransactionBuilder.build(0, 1, 2, 100, keyPair.getPrivate());
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.publicKeys.put(1, keyPair.getPublic());
        transactionManager.wallet.put(1, 100);
        transactionManager.transactionOrder.put(1, 0);
        transactionManager.wallet.put(2, 100);
        transactionManager.performTransaction(transaction);


        assertEquals(0, (int) transactionManager.wallet.get(1));
        assertEquals(200, (int) transactionManager.wallet.get(2));
        assertEquals(1, transactionManager.messages.size());
        int transactionOrderMiner1 = transactionManager.transactionOrder.get(1);
        assertEquals(1, transactionOrderMiner1);
    }

    @Test
    public void updateWallet() {
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.wallet.put(1, 100);
        transactionManager.updateWallet(1, 2, 50);

        assertEquals(100, (int) transactionManager.wallet.get(1));
    }

    @Test
    public void testUpdateWallet() {
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.wallet.put(1, 0);

        assertEquals(0, (int) transactionManager.wallet.get(1));
        transactionManager.updateWallet(1, 100);
        assertEquals(100, (int) transactionManager.wallet.get(1));
    }
}