package external.letiuka.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public final class SHA256HexApacheHasher implements PasswordHasher {
    @Override
    public String getHash(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
