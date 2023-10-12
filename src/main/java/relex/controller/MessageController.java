package relex.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import relex.jwt.JwtUtils;
import relex.model.Message;
import relex.model.User;
import relex.payload.request.GetMessagesRequest;
import relex.payload.request.SendMessageRequest;
import relex.payload.response.GetMessagesResponse;
import relex.payload.response.MessageResponse;
import relex.repository.MessageRepository;
import relex.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class MessageController {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    @PostMapping("/send-message")
    ResponseEntity<?> sendMessage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                  @RequestBody SendMessageRequest sendMessageRequest) {
        token = token.substring(7);
        User userWhoRecieve = userRepository.findByUsername(sendMessageRequest.getUsername());
        User userWhoSend = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
        if (userWhoRecieve != null) {
            if (userWhoRecieve.getId().equals(userWhoSend.getId()))
                return ResponseEntity.status(400).body(new MessageResponse("Incorrect username"));

            Message message = new Message(userWhoSend.getId(), userWhoRecieve.getId(), userWhoSend.getUsername(), userWhoRecieve.getUsername(), sendMessageRequest.getMessage());
            messageRepository.save(message);
            return ResponseEntity.ok(new MessageResponse("Message successfully sended"));
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Can not find user"));
        }
    }

    @PostMapping("/get-messages")
    ResponseEntity<?> getMessages(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                  @RequestBody GetMessagesRequest getMessagesRequest) {
        token = token.substring(7);
        User user1 = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
        User user2 = userRepository.findByUsername(getMessagesRequest.getUsername());
        List<Message> messages1 = messageRepository.findMessageByUserIdWhoSendAndUserIdWhoRecieve(user1.getId(), user2.getId());
        List<Message> messages2 = messageRepository.findMessageByUserIdWhoSendAndUserIdWhoRecieve(user2.getId(), user1.getId());
        List<Message> messages = new ArrayList<>();
        messages.addAll(messages1);
        messages.addAll(messages2);
        return ResponseEntity.ok(new GetMessagesResponse(messages));
    }
}
