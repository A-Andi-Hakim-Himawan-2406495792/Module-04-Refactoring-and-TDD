package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        if (orderRepository.findById(order.getId()).isEmpty()) {
            orderRepository.save(order);
            return order;
        }
        throw new IllegalStateException("Order already exists");
    }

    @Override
    public Order updateStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            Order newOrder = new Order(order.getId(), order.getProducts(),
                    order.getOrderTime(), order.getAuthor(), status);
            orderRepository.save(newOrder);
            return newOrder;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Order findById(UUID orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> findAllByAuthor(String author) {
        return orderRepository.findAllByAuthor(author);
    }

}