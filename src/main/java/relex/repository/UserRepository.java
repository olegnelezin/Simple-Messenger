package relex.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import relex.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET password = :newPassword WHERE username = :username and is_active = true", nativeQuery = true)
    void changePassword(String username, String newPassword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET email = :newEmail WHERE username = :username and is_active = true", nativeQuery = true)
    void changeEmail(String username, String newEmail);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET username = :newUsername WHERE email = :email and is_active = true", nativeQuery = true)
    void changeUsername(String email, String newUsername);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET firstname = :newFirstname WHERE username = :username and is_active = true", nativeQuery = true)
    void changeFirstname(String username, String newFirstname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET lastname = :newLastname WHERE username = :username and is_active = true", nativeQuery = true)
    void changeLastname(String username, String newLastname);

    @Transactional
    void deleteUserByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET is_active = false WHERE username = :username", nativeQuery = true)
    void deactivateUser(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET is_active = true WHERE username = :username", nativeQuery = true)
    void activateUser(String username);
}


