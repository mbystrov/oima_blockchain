package com.griddynamics.blockchain.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.security.*;

public class TransactionBuilderTest {
    @Test
    public void checkTransactionBuilder() throws
            NoSuchAlgorithmException,
            NoSuchProviderException,
            SignatureException,
            InvalidKeyException {
        KeyPair keyPair = KeyPairCreator.createKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        int identifier = 1;
        int senderMinerId = 1;
        int receiverMinerId = 2;
        int amount = 4;
        Transaction transaction = TransactionBuilder.build(identifier, senderMinerId, receiverMinerId, amount, privateKey);

        Assert.assertEquals(identifier, transaction.getIdentifier());
        Assert.assertEquals(senderMinerId, transaction.getSenderMinerId());
        Assert.assertEquals(receiverMinerId, transaction.getReceiverMinerId());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertTrue(KeyPairCreator.verifySignature(transaction.toString()
                .getBytes(), transaction.getSignedTransaction(), keyPair.getPublic()));
    }
}