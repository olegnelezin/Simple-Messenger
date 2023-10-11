package relex.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeInformationRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
}
