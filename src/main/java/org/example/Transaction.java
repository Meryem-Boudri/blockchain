package org.example;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Transaction {
    private String sender;
    private String recipient;
    private double amount;
    private String signature;

    public Transaction(String sender, String recipient, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public Transaction(String sender, String recipient, double amount, String signature) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.signature = signature;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public double getAmount() {
        return amount;
    }

    public String getSignature() {
        return signature;
    }

    public void signTransaction(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String data = sender + recipient + amount;
        Signature signatureAlgorithm = Signature.getInstance("SHA256withECDSA");
        signatureAlgorithm.initSign(privateKey);
        signatureAlgorithm.update(data.getBytes());
        byte[] signedData = signatureAlgorithm.sign();
        this.signature = Base64.getEncoder().encodeToString(signedData);
    }

    public boolean isValidTransaction() {
        String data = sender + recipient + amount;
        try {
            System.out.println("entrain de validé");
            Signature signature = Signature.getInstance("SHA256withECDSA");
            PublicKey publicKey = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(sender)));
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            return signature.verify(Base64.getDecoder().decode(this.signature));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }

}
