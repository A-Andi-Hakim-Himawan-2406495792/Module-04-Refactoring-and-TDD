package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Menggunakan MockitoBean agar tidak muncul warning deprecated
    @MockitoBean
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Produk Dummy");
        product.setProductQuantity(1);
        products.add(product);

        Order order = new Order(UUID.randomUUID(), products, System.currentTimeMillis(), "Andi Hakim");
        payment = new Payment("PAY-123", "VOUCHER_CODE", new HashMap<>(), order);
    }

    @Test
    void testPaymentDetailFormPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detailForm"));
    }

    @Test
    void testPaymentDetailFormSubmitRedirect() throws Exception {
        mockMvc.perform(post("/payment/detail")
                        .param("paymentId", "PAY-123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/detail/PAY-123"));
    }

    @Test
    void testPaymentDetailPage() throws Exception {
        when(paymentService.getPayment("PAY-123")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/PAY-123"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testAdminListPage() throws Exception {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/adminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testAdminDetailPage() throws Exception {
        when(paymentService.getPayment("PAY-123")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/PAY-123"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/adminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testAdminSetStatusPost() throws Exception {
        when(paymentService.getPayment("PAY-123")).thenReturn(payment);
        when(paymentService.setStatus(payment, PaymentStatus.SUCCESS)).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/PAY-123")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }
}