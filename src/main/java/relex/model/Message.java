package relex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @NotNull
    @Column(nullable = false)
    private long userIdWhoSend;

    @JsonIgnore
    @NotNull
    @Column(nullable = false)
    private long userIdWhoRecieve;

    private String usernameWhoSend;
    private String usernameWhoRecieve;
    private String message;
    public Message(long userIdWhoSend, long userIdWhoRecieve, String usernameWhoSend, String usernameWhoRecieve, String message){
        this.userIdWhoSend = userIdWhoSend;
        this.userIdWhoRecieve = userIdWhoRecieve;
        this.usernameWhoSend = usernameWhoSend;
        this.usernameWhoRecieve = usernameWhoRecieve;
        this.message = message;
    }

}
