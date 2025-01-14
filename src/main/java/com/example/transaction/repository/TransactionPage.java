package com.example.transaction.repository;

import com.example.transaction.model.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TransactionPage {
    @JsonProperty("transactions")
    List<Transaction> transactions;
    @JsonProperty("total")
    int total;
    @JsonProperty("page")
    int page;
    @JsonProperty("size")
    int size;
    public TransactionPage(List<Transaction> transactions, int total, int page, int size) {
        this.transactions = transactions;
        this.total = total;
        this.page = page;
        this.size = size;
    }
}
