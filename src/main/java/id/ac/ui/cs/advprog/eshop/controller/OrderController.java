package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "order/create";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "order/history";
    }

    @PostMapping("/history")
    public String showHistoryList(@RequestParam String author, Model model) {
        model.addAttribute("orders", orderService.findAllByAuthor(author));
        model.addAttribute("author", author);
        return "order/historyList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable UUID orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "order/payOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String processPayment(@PathVariable UUID orderId,
                                 @RequestParam String method,
                                 @RequestParam Map<String, String> paymentData,
                                 Model model) {
        Order order = orderService.findById(orderId);
        Payment payment = paymentService.addPayment(order, method, paymentData);

        model.addAttribute("paymentId", payment.getId());
        return "order/paySuccess";
    }
}