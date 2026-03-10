package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(UUID.randomUUID(), new ArrayList<>(), System.currentTimeMillis(), "Andi Hakim");
    }

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/create"));
    }

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history"));
    }

    @Test
    void testOrderHistoryPost() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderService.findAllByAuthor("Andi Hakim")).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                        .param("author", "Andi Hakim"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/historyList"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("author", "Andi Hakim"));
    }

    @Test
    void testPayOrderPage() throws Exception {
        when(orderService.findById(order.getId())).thenReturn(order);

        mockMvc.perform(get("/order/pay/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payOrder"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPayOrderPost() throws Exception {
        when(orderService.findById(order.getId())).thenReturn(order);

        Payment dummyPayment = new Payment("PAY-1", "VOUCHER_CODE", new HashMap<>(), order);
        when(paymentService.addPayment(eq(order), eq("VOUCHER_CODE"), any())).thenReturn(dummyPayment);

        mockMvc.perform(post("/order/pay/" + order.getId())
                        .param("method", "VOUCHER_CODE")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/paySuccess"))
                .andExpect(model().attributeExists("paymentId"));
    }
}