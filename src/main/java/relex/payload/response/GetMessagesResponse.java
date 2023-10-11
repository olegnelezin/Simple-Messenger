package relex.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import relex.model.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMessagesResponse {
    @NotNull
    List<Message> messages;
}
