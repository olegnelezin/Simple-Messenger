package relex.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private final String type = "Bearer";
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;

    public JwtResponse(String accessToken, Long id, String username, String firstname, String lastname, String email) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
}
