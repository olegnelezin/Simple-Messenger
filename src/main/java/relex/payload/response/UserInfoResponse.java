package relex.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
}
