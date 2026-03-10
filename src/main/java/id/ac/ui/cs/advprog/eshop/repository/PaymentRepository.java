package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {
    private List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        // REFACTOR: Menggunakan method set() yang lebih efisien dibandingkan remove() lalu add()
        for (int i = 0; i < paymentData.size(); i++) {
            if (paymentData.get(i).getId().equals(payment.getId())) {
                paymentData.set(i, payment);
                return payment;
            }
        }
        paymentData.add(payment);
        return payment;
    }

    public Optional<Payment> findById(String id) {
        // REFACTOR: Menggunakan Java Stream API untuk pencarian yang lebih rapi
        return paymentData.stream()
                .filter(savedPayment -> savedPayment.getId().equals(id))
                .findFirst();
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(paymentData);
    }
}