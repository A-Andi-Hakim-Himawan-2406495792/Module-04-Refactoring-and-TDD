package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Order order;

    @BeforeEach
    void setUp() {
        // dummy order untuk disambungkan ke payment
        order = new Order(UUID.randomUUID(), null, 1708560000L, "Andi Hakim");
    }

    // --- VOUCHER CODE TESTS ---

    @Test
    void testCreatePaymentVoucherSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("PAY-1", "VOUCHER_CODE", paymentData, order);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedInvalidLength() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123"); // Kurang dari 16

        Payment payment = new Payment("PAY-2", "VOUCHER_CODE", paymentData, order);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedNotStartsWithEshop() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "SALAH1234ABC5678");

        Payment payment = new Payment("PAY-3", "VOUCHER_CODE", paymentData, order);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedNot8Digits() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345BC5678"); // Ada 9 angka

        Payment payment = new Payment("PAY-4", "VOUCHER_CODE", paymentData, order);
        assertEquals("REJECTED", payment.getStatus());
    }

    // --- BANK TRANSFER TESTS ---

    @Test
    void testCreatePaymentBankTransferSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank UI");
        paymentData.put("referenceCode", "REF12345");

        Payment payment = new Payment("PAY-5", "BANK_TRANSFER", paymentData, order);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransferRejectedEmptyBankName() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF12345");

        Payment payment = new Payment("PAY-6", "BANK_TRANSFER", paymentData, order);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransferRejectedNullReference() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank UI");
        paymentData.put("referenceCode", null);

        Payment payment = new Payment("PAY-7", "BANK_TRANSFER", paymentData, order);
        assertEquals("REJECTED", payment.getStatus());
    }
}