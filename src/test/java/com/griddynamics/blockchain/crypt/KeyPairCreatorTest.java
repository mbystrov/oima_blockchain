package com.griddynamics.blockchain.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.security.*;

public class KeyPairCreatorTest {

    @Test
    public void createKeyPairCreatesKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = KeyPairCreator.createKeyPair();

        Assert.assertNotNull("No keypair created", keyPair);
    }

    @Test
    public void signedMessageIsNotEmpty() throws
            NoSuchAlgorithmException,
            NoSuchProviderException,
            SignatureException,
            InvalidKeyException {
        String msg = "message to sign";
        PrivateKey privateKey = KeyPairCreator.createKeyPair().getPrivate();

        Assert.assertTrue(KeyPairCreator.sign(msg, privateKey).length > 0);
    }

    @Test
    public void verifySignature() throws
            NoSuchAlgorithmException,
            NoSuchProviderException,
            SignatureException,
            InvalidKeyException {
        String msg = "message to sign";
        KeyPair keyPair = KeyPairCreator.createKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] signedMsg = KeyPairCreator.sign(msg, privateKey);
        boolean verificationResult = KeyPairCreator.verifySignature(msg.getBytes(), signedMsg, publicKey);

        Assert.assertTrue(verificationResult);
    }

    @Test
    public void verifySignatureReturnsFalseForUnsignedMsg() throws
            NoSuchAlgorithmException,
            NoSuchProviderException,
            SignatureException,
            InvalidKeyException {
        String msg = "message to sign";
        KeyPair keyPair = KeyPairCreator.createKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] signedMsg = KeyPairCreator.sign(msg, privateKey);
        String anotherMsg = "unsigned message";
        boolean verificationResult = KeyPairCreator.verifySignature(anotherMsg.getBytes(), signedMsg, publicKey);

        Assert.assertFalse(verificationResult);
    }

    @Test
    public void verifySignatureReturnsFalseForDifferentKeys() throws
            NoSuchAlgorithmException,
            NoSuchProviderException,
            SignatureException,
            InvalidKeyException {
        String msg = "message to sign";
        PrivateKey privateKey = KeyPairCreator.createKeyPair().getPrivate();
        PublicKey publicKey = KeyPairCreator.createKeyPair().getPublic();
        byte[] signedMsg = KeyPairCreator.sign(msg, privateKey);
        boolean verificationResult = KeyPairCreator.verifySignature(msg.getBytes(), signedMsg, publicKey);

        Assert.assertFalse("publicKey is from a different keyPair", verificationResult);
    }
}