package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    List<Payment> payments;
    Order order;

    @BeforeEach
    void setUp() {
        payments = new ArrayList<>();
        order = new Order(UUID.randomUUID(), new ArrayList<>(), 1708560000L, "Andi Hakim");

        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("PAY-1", "VOUCHER_CODE", paymentData1, order);
        payments.add(payment1);
    }

    @Test
    void testAddPayment() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, payment.getMethod(), payment.getPaymentData());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertNotNull(result);
    }

    @Test
    void testSetStatusSuccessUpdatesOrderStatus() {
        Payment payment = payments.get(0);
        payment.setStatus("WAITING_PAYMENT");

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "SUCCESS");
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", result.getOrder().getStatus()); // Order status should update
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejectedUpdatesOrderStatus() {
        Payment payment = payments.get(0);
        payment.setStatus("WAITING_PAYMENT");

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "REJECTED");
        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", result.getOrder().getStatus()); // Order status should fail
    }

    @Test
    void testGetPaymentIfFound() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetAllPayments() {
        doReturn(payments).when(paymentRepository).getAllPayments();
        List<Payment> result = paymentService.getAllPayments();
        assertEquals(1, result.size());
    }
}