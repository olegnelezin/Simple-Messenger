package relex.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import relex.model.EnumRole;
import relex.model.Role;
import relex.model.User;
import relex.payload.request.RegisterRequest;
import relex.payload.response.MessageResponse;
import relex.repository.RoleRepository;
import relex.repository.UserRepository;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = new User(registerRequest.getUsername(), registerRequest.getFirstname(), registerRequest.getLastname(), registerRequest.getEmail(),
                    encoder.encode(registerRequest.getPassword()));
            Set<String> strRoles = registerRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Role error!"));
                roles.add(userRole);
            }

            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: \n" + e.getMessage());
        }
        return ResponseEntity.status(500).body(new MessageResponse("Not unique or null user data"));
    }
}
