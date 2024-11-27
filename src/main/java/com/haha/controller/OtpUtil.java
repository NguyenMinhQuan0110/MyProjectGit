package com.haha.controller;

import java.security.SecureRandom;


public class OtpUtil {
	// Tạo OTP gồm 6 chữ số ngẫu nhiên
    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // Tạo OTP 6 chữ số
        return String.valueOf(otp);
    }
}
