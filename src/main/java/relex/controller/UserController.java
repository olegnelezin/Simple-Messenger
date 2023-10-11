package relex.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import relex.jwt.JwtUtils;
import relex.model.User;
import relex.payload.request.ActivateAccountRequest;
import relex.payload.request.ChangeInformationRequest;
import relex.payload.request.ChangePasswordRequest;
import relex.payload.response.MessageResponse;
import relex.payload.response.UserInfoResponse;
import relex.repository.UserRepository;
import relex.service.LogoutService;


@AllArgsConstructor
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final LogoutService logoutService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;

    @PostMapping("/change-information")
    public ResponseEntity<?> changeUserInformation(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                  @RequestBody ChangeInformationRequest changeInformationRequest) {
        token = token.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(token);

        String newEmail = changeInformationRequest.getEmail();
        if (newEmail != null)
            userRepository.changeEmail(username, newEmail);

        String newFirstname = changeInformationRequest.getFirstname();
        if (newFirstname != null)
            userRepository.changeFirstname(username, newFirstname);

        String newLastname = changeInformationRequest.getLastname();
        if (newLastname != null)
            userRepository.changeLastname(username, newLastname);

        String newUsername = changeInformationRequest.getUsername();
        if (newUsername != null)
            userRepository.changeUsername(newEmail, newUsername);

        return ResponseEntity.ok(new MessageResponse("You changed your profile information."));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                            @RequestBody ChangePasswordRequest passwordRequest) {
        token = token.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        userRepository.changePassword(username, encoder.encode(passwordRequest.getNew_password()));
        return ResponseEntity.ok(new MessageResponse("You changed your password."));
    }

    @GetMapping("/signout")
    public ResponseEntity<?> signOutFromAccount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        token = token.substring(7);
        logoutService.invalidateToken(token);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        return ResponseEntity.ok(new MessageResponse("You successfully logout!"));
    }

    @GetMapping("/deactivate")
    public ResponseEntity<?> deactivateAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = token.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        userRepository.deactivateUser(username);
        return ResponseEntity.ok(new MessageResponse("Your account successfully deactivated. You can restore account by /activate"));
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestBody ActivateAccountRequest activateAccountRequest) {
        String username = activateAccountRequest.getUsername();
        User user = userRepository.findByUsername(username);
        String password = activateAccountRequest.getPassword();
        if (user.isActive()) {
            return ResponseEntity.status(400).body(new MessageResponse("Account already active!"));
        } else if (user.getUsername() == null) {
            return ResponseEntity.status(400).body(new MessageResponse("Unknown user. Activate is failed."));
        } else if (encoder.encode(password).equals(user.getPassword())) {
            return ResponseEntity.status(400).body(new MessageResponse("Invalid password. Try again."));
        }
        userRepository.activateUser(username);
        return ResponseEntity.ok(new MessageResponse("Account successfully activated!"));
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = token.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        userRepository.deleteUserByUsername(username);
        return ResponseEntity.ok(new MessageResponse("Your account successfully deleted"));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInformation(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = token.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userRepository.findByUsername(username);
        UserInfoResponse userInfoResponse = new UserInfoResponse(user.getUsername(), user.getFirstname(),
                user.getLastname(), user.getEmail());
        return ResponseEntity.ok(userInfoResponse);
    }
}

