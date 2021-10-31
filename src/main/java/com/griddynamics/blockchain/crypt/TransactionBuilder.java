package com.griddynamics.blockchain.crypt;

import java.security.PrivateKey;

public class TransactionBuilder {
    /**
     * Building a transaction using sender miner's private RSA key
     *
     * @param identifier      number of miner's transaction
     * @param senderMinerId   id of miner who sent a transaction
     * @param receiverMinerId id of miner of a miner who is intended to receive a transacion
     * @param amount          amount of VC
     * @param privateKey      private key of sender miner
     * @return a transaction
     */
    public static Transaction build(int identifier,
                                    int senderMinerId,
                                    int receiverMinerId,
                                    int amount,
                                    PrivateKey privateKey) {
        Transaction transaction = new Transaction(identifier, senderMinerId, receiverMinerId, amount);
        String transactionString = transaction.toString();
        try {
            byte[] signedTransaction = KeyPairCreator.sign(transactionString, privateKey);
            transaction.setSignedTransaction(signedTransaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transaction;
    }
}
