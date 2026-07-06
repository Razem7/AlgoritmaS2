package com.kasir.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    private String id;
    
    @Column(nullable = false)
    private LocalDateTime date;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "transaction")
    private List<TransactionItem> items = new ArrayList<>();
    
    @Column(nullable = false)
    private double total;
    
    @Column(nullable = false)
    private double payment;
    
    @Column(name = "kembalian", nullable = false)
    private double change;

    public Transaction() {
    }

    public Transaction(String id, LocalDateTime date, List<CartItem> cartItems, double total, double payment, double change) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.payment = payment;
        this.change = change;
        
        // Convert CartItems to TransactionItems
        this.items = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            TransactionItem item = new TransactionItem(
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity()
            );
            item.setTransaction(this);
            this.items.add(item);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items;
        for (TransactionItem item : items) {
            item.setTransaction(this);
        }
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
