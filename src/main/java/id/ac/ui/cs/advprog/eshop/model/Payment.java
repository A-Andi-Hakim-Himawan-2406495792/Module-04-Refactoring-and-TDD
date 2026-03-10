package id.ac.ui.cs.advprog.eshop.model;

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

        if ("VOUCHER_CODE".equals(method)) {
            validateVoucherCode(paymentData.get("voucherCode"));
        } else if ("BANK_TRANSFER".equals(method)) {
            validateBankTransfer(paymentData.get("bankName"), paymentData.get("referenceCode"));
        } else {
            this.status = "REJECTED";
        }
    }

    private void validateVoucherCode(String voucher) {
        if (voucher != null && voucher.length() == 16 && voucher.startsWith("ESHOP") && countDigits(voucher) == 8) {
            this.status = "SUCCESS";
        } else {
            this.status = "REJECTED";
        }
    }

    private void validateBankTransfer(String bankName, String referenceCode) {
        if (bankName != null && !bankName.trim().isEmpty() && referenceCode != null && !referenceCode.trim().isEmpty()) {
            this.status = "SUCCESS";
        } else {
            this.status = "REJECTED";
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