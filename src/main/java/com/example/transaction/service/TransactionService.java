package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionPage;
import com.example.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void createTransaction(Transaction transaction) throws InterruptedException {
        transactionRepository.insert(transaction);
    }

    public void deleteTransaction(int id) throws InterruptedException {
        transactionRepository.delete(id);
    }

    public void modifyTransaction(Transaction updatedTransaction) throws InterruptedException {
        transactionRepository.update(updatedTransaction);
    }

    public TransactionPage search(int start, int size) throws InterruptedException{
        return transactionRepository.search(start, size);
    }
}