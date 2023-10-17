package relex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import relex.model.EnumRole;
import relex.model.Role;
import relex.repository.RoleRepository;

import java.util.Optional;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository repository;

    public Optional<Role> findByName(EnumRole name) {
        return this.repository.findByName(name);
    }
}
