package com.kasir.controller;

import com.kasir.model.CartItem;
import com.kasir.model.Product;
import com.kasir.model.Transaction;
import com.kasir.service.ProductService;
import com.kasir.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class KasirController {

    @Autowired
    private ProductService productService;

    @Autowired
    private TransactionService transactionService;

    private List<CartItem> cart = new ArrayList<>();

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cart", cart);
        model.addAttribute("total", calculateTotal());
        return "index";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam String productId, @RequestParam int quantity) {
        Product product = productService.getProductById(productId).orElse(null);
        
        if (product != null && product.getStock() >= quantity) {
            // Cek apakah produk sudah ada di cart
            CartItem existingItem = cart.stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                cart.add(new CartItem(product, quantity));
            }
        }

        return "redirect:/";
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam String productId) {
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
        return "redirect:/";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam double payment, Model model) {
        double total = calculateTotal();
        
        if (payment < total) {
            model.addAttribute("error", "Pembayaran kurang!");
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("cart", cart);
            model.addAttribute("total", total);
            return "index";
        }

        double change = payment - total;

        // Update stok produk
        for (CartItem item : cart) {
            productService.updateStock(item.getProduct().getId(), item.getQuantity());
        }

        // Simpan transaksi
        Transaction transaction = new Transaction(
                transactionService.generateTransactionId(),
                LocalDateTime.now(),
                new ArrayList<>(cart),
                total,
                payment,
                change
        );
        transactionService.saveTransaction(transaction);

        // Reset cart
        cart.clear();

        model.addAttribute("transaction", transaction);
        return "receipt";
    }

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        return "history";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String id, @RequestParam String name, 
                            @RequestParam double price, @RequestParam int stock) {
        productService.addProduct(new Product(id, name, price, stock));
        return "redirect:/products";
    }

    @PostMapping("/products/update")
    public String updateProduct(@RequestParam String id, @RequestParam String name, 
                               @RequestParam double price, @RequestParam int stock) {
        productService.updateProduct(id, name, price, stock);
        return "redirect:/products";
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam String productId) {
        productService.deleteProduct(productId);
        return "redirect:/products";
    }

    @GetMapping("/report")
    public String report(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        model.addAttribute("totalRevenue", transactionService.getTotalRevenue());
        model.addAttribute("totalTransactions", transactionService.getTotalTransactions());
        model.addAttribute("averageTransaction", transactionService.getAverageTransaction());
        model.addAttribute("topProducts", transactionService.getTopProducts());
        return "report";
    }

    @GetMapping("/export/transactions")
    @ResponseBody
    public void exportTransactions(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.csv");
        
        PrintWriter writer = response.getWriter();
        writer.println("ID Transaksi,Tanggal,Total,Pembayaran,Kembalian");
        
        for (Transaction transaction : transactionService.getAllTransactions()) {
            writer.println(String.format("%s,%s,%.2f,%.2f,%.2f",
                    transaction.getId(),
                    transaction.getDate(),
                    transaction.getTotal(),
                    transaction.getPayment(),
                    transaction.getChange()));
        }
        
        writer.flush();
    }

    private double calculateTotal() {
        return cart.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }
}
