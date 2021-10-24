package com.griddynamics.blockchain.crypt;

import java.security.*;

/**
 * How to use:
 * <blockquote><pre>
 * {@code
 * String msg = "message to sign";
 * KeyPair keyPair = KeyPairCreator.createKeyPair();
 * PublicKey publicKey = keyPair.getPublic();
 * PrivateKey privateKey = keyPair.getPrivate();
 * byte[] signedMsg = KeyPairCreator.sign(msg, privateKey);
 * boolean verificationResult = KeyPairCreator.verifySignature(msg.getBytes(), signedMsg, publicKey);
 * }
 * </pre></blockquote>
 */
public class KeyPairCreator {
    private static final String RNG_ALGORITHM = "SHA1PRNG";
    private static final String RSA_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String PROVIDER = "SUN";

    public static KeyPair createKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance(RNG_ALGORITHM, PROVIDER);
        keyGen.initialize(1024, random);
        return keyGen.generateKeyPair();
    }

    public static byte[] sign(String msg, PrivateKey privateKey) throws
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Signature rsa = Signature.getInstance(SIGNATURE_ALGORITHM);
        rsa.initSign(privateKey);
        rsa.update(msg.getBytes());
        return rsa.sign();
    }

    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(data);

        return sig.verify(signature);
    }
}

