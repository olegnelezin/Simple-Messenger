package relex.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import relex.repository.LogoutRepository;

@Slf4j
@Service
public class LogoutService {
    private final LogoutRepository logoutRepository;
    public LogoutService(LogoutRepository logoutRepository) {
        this.logoutRepository = logoutRepository;
    }

    public boolean containsValue(String value) {
        return logoutRepository.containsValue(value);
    }

    public void invalidateToken(String token) {
        if (logoutRepository.containsValue(token)) {
            log.error("This token already in black list.");
        } else {
            logoutRepository.put(token, 600);
        }
    }
}
