package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
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

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"));
        product.setProductName("Produk Dummy");
        product.setProductQuantity(1);
        products.add(product);


        order = new Order(UUID.randomUUID(), products, 1708560000L, "Andi Hakim");


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

        // REFACTOR: Gunakan status yang valid untuk Payment (REJECTED atau SUCCESS)
        payment.setStatus(PaymentStatus.REJECTED.getValue());

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(payment);

        // REFACTOR: Gunakan Enum saat memanggil fungsi setStatus dari service
        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), result.getOrder().getStatus()); // Order harus jadi SUCCESS
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejectedUpdatesOrderStatus() {
        Payment payment = payments.get(0);

        // REFACTOR: Gunakan status yang valid untuk Payment
        payment.setStatus(PaymentStatus.SUCCESS.getValue());

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(payment);

        // REFACTOR: Gunakan Enum saat memanggil fungsi setStatus dari service
        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), result.getOrder().getStatus()); // Order harus jadi FAILED
        verify(paymentRepository, times(1)).save(payment);
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