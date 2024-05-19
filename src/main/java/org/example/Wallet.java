package org.example;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private double balance;

    public Wallet() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        KeyPair keyPair = KeyPairGeneratorUtil.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        this.balance=10.0;
    }

    public String getAddress() {
         return Base64.getEncoder().encodeToString(publicKey.getEncoded());

    }
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void signTransaction(Transaction transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        transaction.signTransaction(privateKey);
    }
}
