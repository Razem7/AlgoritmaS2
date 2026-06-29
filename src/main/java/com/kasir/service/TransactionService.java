package com.kasir.service;

import com.kasir.model.Transaction;
import com.kasir.model.TransactionItem;
import com.kasir.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public String generateTransactionId() {
        return "TRX" + System.currentTimeMillis();
    }

    public double getTotalRevenue() {
        return transactionRepository.findAll().stream()
                .mapToDouble(Transaction::getTotal)
                .sum();
    }

    public int getTotalTransactions() {
        return (int) transactionRepository.count();
    }

    public double getAverageTransaction() {
        long count = transactionRepository.count();
        if (count == 0) {
            return 0;
        }
        return getTotalRevenue() / count;
    }

    public Map<String, Integer> getTopProducts() {
        Map<String, Integer> productSales = new HashMap<>();
        
        for (Transaction transaction : transactionRepository.findAll()) {
            for (TransactionItem item : transaction.getItems()) {
                String productName = item.getProductName();
                productSales.put(productName, 
                    productSales.getOrDefault(productName, 0) + item.getQuantity());
            }
        }
        
        return productSales.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
    }
}
