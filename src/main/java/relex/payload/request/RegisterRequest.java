package relex.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;
@Getter
@Setter
public class RegisterRequest {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String email;
    private Set<String> role;
}
