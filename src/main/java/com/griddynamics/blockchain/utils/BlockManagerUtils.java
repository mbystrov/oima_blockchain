package com.griddynamics.blockchain.utils;

import com.griddynamics.blockchain.crypt.KeyPairCreator;
import com.griddynamics.blockchain.crypt.Transaction;

import java.security.PublicKey;

public class BlockManagerUtils {
    /**
     * Verifying transaction
     * @param transaction transaction received from Miner
     * @param publicKey Miner's RSA public key
     * @param transactionNumber number of the transaction
     * @return boolean status
     */
    public static boolean verifyTransaction(Transaction transaction, PublicKey publicKey, int transactionNumber) {
        int identifier = transaction.getIdentifier();
        byte[] signedTransaction = transaction.getSignedTransaction();
        String transactionString = transaction.toString();
        if (transactionNumber == identifier) {
            try {
                return KeyPairCreator.verifySignature(transactionString.getBytes(), signedTransaction, publicKey);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
