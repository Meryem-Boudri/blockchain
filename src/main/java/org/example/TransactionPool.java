package org.example;

import java.util.ArrayList;

public class TransactionPool {
    private ArrayList<Transaction> pendingTransactions;

    public TransactionPool() {
        this.pendingTransactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        this.pendingTransactions.add(transaction);
    }

    public ArrayList<Transaction> getPendingTransactions() {
        return this.pendingTransactions;
    }

    public void clear() {
       pendingTransactions.clear();
    }
    public void removeTransaction(Transaction transaction) {
        pendingTransactions.remove(transaction);
    }
}
