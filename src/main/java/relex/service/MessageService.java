package relex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import relex.model.Message;
import relex.repository.MessageRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class MessageService {
    private final MessageRepository repository;

     public List<Message> findMessages(long userId1, long userId2) {
        return this.repository.findMessageByUserIdWhoSendAndUserIdWhoRecieveOrUserIdWhoRecieveAndUserIdWhoSend(userId1, userId2, userId1, userId2);
    }

    public void save(Message message) {
         this.repository.save(message);
    }
}
