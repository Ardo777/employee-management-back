package am.itspace.employeemanagement.util;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.UUID;

@Component
public class VerificationCodeUtil {

    public String createVerificationCode() {
        String lUUID = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        return lUUID.substring(0, Math.min(lUUID.length(), 6));
    }
}
