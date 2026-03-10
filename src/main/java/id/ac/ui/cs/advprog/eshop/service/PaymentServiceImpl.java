package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, method, paymentData, order);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        Payment savedPayment = paymentRepository.findById(payment.getId());
        if (savedPayment != null) {
            savedPayment.setStatus(status);

            // Sync status pesanan sesuai deskripsi fitur
            if ("SUCCESS".equals(status)) {
                savedPayment.getOrder().setStatus("SUCCESS");
            } else if ("REJECTED".equals(status)) {
                savedPayment.getOrder().setStatus("FAILED");
            }

            paymentRepository.save(savedPayment);
            return savedPayment;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }
}