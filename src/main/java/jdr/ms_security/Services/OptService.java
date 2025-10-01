package jdr.ms_security.Services;


import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OptService {

    private static final int OTP_LENGTH = 6;
    private static final String DIGITS = "0123456789";
    private final SecureRandom random = new SecureRandom();

    /**
     * Genera un código OTP de 6 dígitos aleatorio.
     * @return El código OTP como String.
     */
    public String generateOtp() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return otp.toString();
    }
}
