package com.example.transaction.controller;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionPage;
import com.example.transaction.repository.TransactionRepository;
import com.example.transaction.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity createTransaction(@RequestBody Transaction transaction) {
        if(!isValidTransaction(transaction)) {
            return ResponseEntity.badRequest().build();
        }
        try{
            transactionService.createTransaction(transaction);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id) {
        try{
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping()
    public ResponseEntity<Transaction> modifyTransaction(@RequestBody Transaction updatedTransaction) {
        if(!isValidTransaction(updatedTransaction)) {
            return ResponseEntity.badRequest().build();
        }
        try{
            transactionService.modifyTransaction(updatedTransaction);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<TransactionPage> search(@RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        if (page < 1 || size < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            int start = (page - 1) * size + 1;
            TransactionPage transactionPage = transactionService.search(start,size);
            return ResponseEntity.ok(transactionPage);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidTransaction(Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            return false;
        }
        return true;
    }
}