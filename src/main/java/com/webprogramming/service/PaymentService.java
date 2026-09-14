package com.webprogramming.service;

import org.springframework.stereotype.Service;

/**
 * Mo phong cong thanh toan online phuc vu bai tap Sequence Diagram.
 * Trong he thong that, lop nay se goi API cua cong thanh toan.
 */
@Service
public class PaymentService {

    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_FAILED = "FAILED";

    /**
     * Xu ly ket qua thanh toan mo phong.
     * @return true neu thanh toan thanh cong, false neu that bai.
     */
    public boolean processPayment(double amount, String result) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Tong tien thanh toan khong hop le");
        }

        if (RESULT_SUCCESS.equalsIgnoreCase(result)) {
            return true;
        }
        if (RESULT_FAILED.equalsIgnoreCase(result)) {
            return false;
        }

        throw new IllegalArgumentException("Ket qua thanh toan khong hop le");
    }
}
