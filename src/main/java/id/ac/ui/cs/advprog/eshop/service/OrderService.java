package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    public Order createOrder(Order order);
    public Order updateStatus(UUID orderId, String status); // Parameter UUID
    public Order findById(UUID orderId); // Parameter UUID
    public List<Order> findAllByAuthor(String author);
}