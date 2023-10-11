package relex.model;

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

    @NotNull
    @Column(nullable = false)
    private long userIdWhoSend;

    @NotNull
    @Column(nullable = false)
    private long userIdWhoRecieve;

    private String message;
    public Message(long userIdWhoSend, long userIdWhoRecieve, String message){
        this.userIdWhoSend = userIdWhoSend;
        this.userIdWhoRecieve = userIdWhoRecieve;
        this.message = message;
    }

}
