package org.example;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blockchain {
    private List<Block> chain;
    private TransactionPool transactionPool;
    private int difficulty;

    private P2PNetwork network;
    private Map<String, Double> balances;



    public Blockchain(int difficulty, int port) throws IOException {
        this.balances = new HashMap<>();
        this.chain = new ArrayList<>();
        this.transactionPool = new TransactionPool();
        this.difficulty = difficulty;
        this.network = new P2PNetwork(port);

        this.chain.add(generateGenesisBlock());
    }

    private Block generateGenesisBlock() {

        Block genesisBlock = new Block(0, System.currentTimeMillis(), "0", new ArrayList<>(), 0);
        genesisBlock.mineBlock(difficulty);
        return genesisBlock;
    }

    public Block getLatestBlock() {
        return this.chain.get(this.chain.size() - 1);
    }

    public Block getBlockByIndex(int index) {
        return this.chain.get(index);
    }

    public void addBlock(Block newBlock) {
        Block latestBlock = getLatestBlock();
        newBlock.setPreviousHash(latestBlock.getCurrentHash());
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }
    public boolean validateChain() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getCurrentHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            if (!currentBlock.getPreviousHash().equals(previousBlock.getCurrentHash())) {
                return false;
            }
        }
        return true;
    }
    public void mineBlock(Block block, int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!block.getCurrentHash().substring(0, difficulty).equals(target)) {
            block.setCurrentHash(block.calculateHash());
        }
    }


    // Méthode pour récupérer le solde d'un utilisateur
    public double getBalance(String publicKey) {
        return balances.getOrDefault(publicKey, 0.0);
    }
    public void adjustDifficulty() {
        if (chain.size() % difficulty == 0 && chain.size() != 0) {
            Block lastAdjustmentBlock = chain.get(chain.size() - difficulty);
            long timeExpected = difficulty * 10; // assuming 10 seconds per block
            long timeTaken = getLatestBlock().getTimestamp() - lastAdjustmentBlock.getTimestamp();

            if (timeTaken < timeExpected / 2) {
                difficulty++;
            } else if (timeTaken > timeExpected * 2) {
                difficulty--;
            }

            System.out.println("Difficulty adjusted to: " + difficulty);
        }
    }

    public double getBalance1(String publicKey) {
        double balance = 100;

        ArrayList<Transaction> transactions = getTransactionPool().getPendingTransactions();
        for (Transaction transaction : transactions) {
            if (transaction.getSender().equals(publicKey)) {
                balance -= transaction.getAmount(); // Soustraire le montant de la transaction du solde
            }
            if (transaction.getRecipient().equals(publicKey)) {
                balance += transaction.getAmount(); // Ajouter le montant de la transaction au solde
            }
        }

        return balance;
    }
    public void addTransaction(Transaction transaction) {
        System.out.println("avant de valider");
        if (transaction.isValidTransaction()) {
            System.out.println("transaction validée");
            transactionPool.addTransaction(transaction);
            // Mettre à jour les soldes des utilisateurs
            String sender = transaction.getSender();
            String recipient = transaction.getRecipient();
            double amount = transaction.getAmount();

            // Décrémenter le solde de l'expéditeur
            double senderBalance = balances.getOrDefault(sender, 0.0);
            balances.put(sender, senderBalance - amount);

            // Incrémenter le solde du destinataire
            double recipientBalance = balances.getOrDefault(recipient, 0.0);
            balances.put(recipient, recipientBalance + amount);
            network.broadcast("New transaction: " + transaction);

        }
    }

//    public List<Transaction> getPendingTransactions() {
//        return transactionPool.getPendingTransactions();
//    }
//
    public void processPendingTransactions() {
        List<Transaction> transactions = transactionPool.getPendingTransactions();
        Block newBlock = new Block(chain.size(), System.currentTimeMillis(), getLatestBlock().getCurrentHash(), getTransactionPool().getPendingTransactions(), 0);
        addBlock(newBlock);
        network.broadcast("New block: " + newBlock);

        transactionPool.clear();
    }

//    public void handleIncomingBlock(Block block) {
//        if (validateBlock(block, getLatestBlock())) {
//            chain.add(block);
//            updateBalances(block);
//            System.out.println("Block added to the chain: " + block);
//        } else {
//            System.out.println("Invalid block received, rejected.");
//        }
//    }
//
//    public void handleIncomingTransaction(Transaction transaction) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
//        addTransaction(transaction);
//        System.out.println("Transaction added to the pool: " + transaction);
//    }

    public TransactionPool getTransactionPool() {
        return transactionPool;
    }
}
