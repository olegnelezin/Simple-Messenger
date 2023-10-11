package relex.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import relex.jwt.JwtUtils;
import relex.model.Message;
import relex.model.User;
import relex.payload.request.SendMessageRequest;
import relex.payload.response.MessageResponse;
import relex.repository.MessageRepository;
import relex.repository.UserRepository;

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

            Message message = new Message(userWhoSend.getId(), userWhoRecieve.getId(), sendMessageRequest.getMessage());
            messageRepository.save(message);
            return ResponseEntity.ok(new MessageResponse("Message successfully sended"));
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Can not find user"));
        }
    }

    @GetMapping("/get-messages")
    ResponseEntity<?> getMessages(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = token.substring(7);
        User user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
        List<Message> sendedMessages = messageRepository.findMessageByUserIdWhoSend(user.getId());
        List<Message> recievedMessages = messageRepository.findMessageByUserIdWhoRecieve(user.getId());
        StringBuilder messages = new StringBuilder();
        messages.append("Sended Messages:\n");
        for (int i = 0; i < sendedMessages.size(); i++) {
            messages.append(sendedMessages.get(0).getMessage()).append("\n");
        }
        messages.append("\n").append("Recieved Messages:").append("\n");
        for (int i = 0; i < recievedMessages.size(); i++) {
            messages.append(recievedMessages.get(i).getMessage()).append("\n");
        }
        return ResponseEntity.ok(messages);
    }
}
