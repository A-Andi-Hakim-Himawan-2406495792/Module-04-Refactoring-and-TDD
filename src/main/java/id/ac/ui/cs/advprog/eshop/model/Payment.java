package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, Map<String, String> paymentData, Order order) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.order = order;

        // REFACTOR: Menggunakan Enum pengganti string hardcode
        if (PaymentMethod.VOUCHER_CODE.getValue().equals(method)) {
            validateVoucherCode(paymentData.get("voucherCode"));
        } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(method)) {
            validateBankTransfer(paymentData.get("bankName"), paymentData.get("referenceCode"));
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    public void setStatus(String status) {
        // REFACTOR: Menambahkan validasi status menggunakan Enum
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void validateVoucherCode(String voucher) {
        if (voucher != null && voucher.length() == 16 && voucher.startsWith("ESHOP") && countDigits(voucher) == 8) {
            this.status = PaymentStatus.SUCCESS.getValue();
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    private void validateBankTransfer(String bankName, String referenceCode) {
        if (bankName != null && !bankName.trim().isEmpty() && referenceCode != null && !referenceCode.trim().isEmpty()) {
            this.status = PaymentStatus.SUCCESS.getValue();
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    private int countDigits(String str) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                count++;
            }
        }
        return count;
    }
}