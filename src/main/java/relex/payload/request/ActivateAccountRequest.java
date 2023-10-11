package relex.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ActivateAccountRequest {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
