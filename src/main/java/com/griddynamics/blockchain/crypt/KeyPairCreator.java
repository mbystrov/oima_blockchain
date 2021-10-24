package com.griddynamics.blockchain.crypt;

import java.security.*;

public class KeyPairCreator {
    public static KeyPair createKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);

        return keyGen.generateKeyPair();
    }

    public static byte[] sign(String msg, PrivateKey privateKey) throws Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        rsa.update(msg.getBytes());
        return rsa.sign();
    }

    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(data);

        return sig.verify(signature);
    }

    public static void main(String[] args) throws Exception {
        KeyPair kp = createKeyPair();
        String msg = "String";
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();

        byte[] signedMsg = sign(msg, privateKey);
        byte[] byteMsg = msg.getBytes();
        boolean isValid = verifySignature(byteMsg, signedMsg, publicKey);
        System.out.println(isValid);
    }
}

