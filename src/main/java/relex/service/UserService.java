package relex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import relex.model.User;
import relex.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public User getUserByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    public void save(User user) {
        this.repository.save(user);
    }

    public void changePassword(String username, String newPassword) {
        this.repository.changePassword(username, newPassword);
    }

    public void changeEmail(String username, String newEmail) {
        this.repository.changeEmail(username, newEmail);
    }

    public void changeUsername(String email, String newUsername) {
        this.repository.changeUsername(email, newUsername);
    }
    public void changeFirstname(String username, String newFirstname) {
        this.repository.changeFirstname(username, newFirstname);
    }

    public void changeLastname(String username, String newLastname) {
        this.repository.changeLastname(username, newLastname);
    }

    public void deleteUserByUsername(String username) {
        this.repository.deleteUserByUsername(username);
    }

    public void deactivateUser(String username) {
        this.repository.deactivateUser(username);
    }

    public void activateUser(String username) {
        this.repository.deactivateUser(username);
    }
}
