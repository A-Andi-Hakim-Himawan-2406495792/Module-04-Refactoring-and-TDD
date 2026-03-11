package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;
    Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        payments = new ArrayList<>();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Produk Dummy");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order(UUID.randomUUID(), products, 1708560000L, "Andi Hakim");

        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("PAY-1", "VOUCHER_CODE", paymentData1, order);
        payments.add(payment1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "Bank UI");
        paymentData2.put("referenceCode", "REF123");
        Payment payment2 = new Payment("PAY-2", "BANK_TRANSFER", paymentData2, order);
        payments.add(payment2);
    }

    @Test
    void testSaveCreate() {
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payments.get(0).getId()).orElseThrow();
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        Payment payment = payments.get(0);
        paymentRepository.save(payment);

        Payment newPayment = new Payment(payment.getId(), payment.getMethod(), payment.getPaymentData(), payment.getOrder());
        newPayment.setStatus("SUCCESS");
        Payment result = paymentRepository.save(newPayment);

        Payment findResult = paymentRepository.findById(payment.getId()).orElseThrow();
        assertEquals(payment.getId(), result.getId());
        assertEquals("SUCCESS", findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById(payments.get(1).getId()).orElseThrow();
        assertEquals(payments.get(1).getId(), findResult.getId());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Optional<Payment> findResult = paymentRepository.findById("PAY-INVALID");
        assertTrue(findResult.isEmpty());
    }

    @Test
    void testGetAllPayments() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        List<Payment> paymentList = paymentRepository.getAllPayments();
        assertEquals(2, paymentList.size());
    }
}