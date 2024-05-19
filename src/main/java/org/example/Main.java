package org.example;

import java.security.*;
import java.util.Base64;

public class Main{
    public static void main(String[] args) throws Exception {
        // Initialize blockchain and P2P network
        Blockchain blockchain = new Blockchain(2, 8080);

        // Generate key pairs for users
        KeyPair keyPairAlice = KeyPairGeneratorUtil.generateKeyPair();
        KeyPair keyPairBob = KeyPairGeneratorUtil.generateKeyPair();
        KeyPair keyPairCharlie = KeyPairGeneratorUtil.generateKeyPair();

        // Get encoded public keys (addresses)
        String addressAlice = Base64.getEncoder().encodeToString(keyPairAlice.getPublic().getEncoded());
        String addressBob = Base64.getEncoder().encodeToString(keyPairBob.getPublic().getEncoded());
        String addressCharlie = Base64.getEncoder().encodeToString(keyPairCharlie.getPublic().getEncoded());

        // Create transactions
        Transaction transaction1 = new Transaction(addressAlice, addressBob, 10.0);
        transaction1.signTransaction(keyPairAlice.getPrivate());

        Transaction transaction2 = new Transaction(addressBob, addressCharlie, 5.0);
        transaction2.signTransaction(keyPairBob.getPrivate());

        Transaction transaction3 = new Transaction(addressCharlie, addressAlice, 3.0);
        transaction3.signTransaction(keyPairCharlie.getPrivate());

        // Add transactions to the blockchain
        blockchain.addTransaction(transaction1);
        blockchain.addTransaction(transaction2);
        blockchain.addTransaction(transaction3);

        // Process pending transactions and mine a new block
        blockchain.processPendingTransactions();

        // Validate the blockchain
        blockchain.validateChain();

        // Add transactions to the blockchain
        blockchain.addTransaction(transaction1);
        blockchain.addTransaction(transaction2);
        blockchain.addTransaction(transaction3);

        // Process pending transactions and mine a new block
        blockchain.processPendingTransactions();

        // Validate the blockchain
        blockchain.validateChain();

        // Print balances
        System.out.println("Alice's balance: " + blockchain.getBalance(addressAlice));
        System.out.println("Bob's balance: " + blockchain.getBalance(addressBob));
        System.out.println("Charlie's balance: " + blockchain.getBalance(addressCharlie));
    }
}
