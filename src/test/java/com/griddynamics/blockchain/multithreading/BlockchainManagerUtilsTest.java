package com.griddynamics.blockchain.multithreading;

import com.griddynamics.blockchain.crypt.KeyPairCreator;
import com.griddynamics.blockchain.crypt.Transaction;
import com.griddynamics.blockchain.crypt.TransactionBuilder;
import com.griddynamics.blockchain.utils.BlockManagerUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.*;

public class BlockchainManagerUtilsTest {
    private static PublicKey publicKey;
    private static int transactionNumber;
    private static Transaction transaction;

    @BeforeClass
    public static void beforeClass() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = KeyPairCreator.createKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        transactionNumber = 0;
        transaction = TransactionBuilder.build(transactionNumber, 1, 2, 50, privateKey);
    }

    @Test
    public void checkVerifyTransaction() {
        boolean verificationResult = BlockManagerUtils.verifyTransaction(transaction, publicKey, transactionNumber);

        Assert.assertTrue(verificationResult);
    }

    @Test
    public void checkVerifyTransactionReturnsFailIfWrongTransactionNumber() {
        int incorrectTransactionNumber = 1;
        boolean verificationResult = BlockManagerUtils.verifyTransaction(transaction, publicKey, incorrectTransactionNumber);

        Assert.assertFalse("transaction number should be as it specified at creation of the transaction", verificationResult);
    }

    @Test
    public void checkVerifyTransactionReturnsFailIfWrongPublicKey() throws
            NoSuchAlgorithmException,
            NoSuchProviderException {
        PublicKey incorrectPublicKey = KeyPairCreator.createKeyPair().getPublic();
        boolean verificationResult = BlockManagerUtils.verifyTransaction(transaction, incorrectPublicKey, transactionNumber);

        Assert.assertFalse("public key should be from the same key pair as a private key", verificationResult);
    }
}