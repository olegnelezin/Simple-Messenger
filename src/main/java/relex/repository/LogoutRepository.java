package relex.repository;

import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LogoutRepository {
    private final ExpiringMap<Integer, String> tokenEventMap;

    public LogoutRepository() {
        tokenEventMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(10)
                .build();
    }
    public void put(String token, long time) {
        tokenEventMap.put(0, token, time, TimeUnit.SECONDS);
    }

    public boolean containsValue(String value) {
        return tokenEventMap.containsValue(value);
    }

}
