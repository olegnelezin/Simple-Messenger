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
import relex.service.MessageService;
import relex.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class MessageController {
    private MessageService messageService;
    private UserService userService;
    private final JwtUtils jwtUtils;
    @PostMapping("/send-message")
    ResponseEntity<?> sendMessage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                  @RequestBody SendMessageRequest sendMessageRequest) {
        token = token.substring(7);
        User userWhoRecieve = userService.getUserByUsername(sendMessageRequest.getUsername());
        User userWhoSend = userService.getUserByUsername(jwtUtils.getUserNameFromJwtToken(token));
        if (userWhoRecieve != null) {
            if (userWhoRecieve.getId().equals(userWhoSend.getId()))
                return ResponseEntity.status(400).body(new MessageResponse("Incorrect username"));

            Message message = new Message(userWhoSend.getId(), userWhoRecieve.getId(), userWhoSend.getUsername(), userWhoRecieve.getUsername(), sendMessageRequest.getMessage());
            messageService.save(message);
            return ResponseEntity.ok(new MessageResponse("Message successfully sended"));
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Can not find user"));
        }
    }

    @PostMapping("/get-messages")
    ResponseEntity<?> getMessages(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                  @RequestBody GetMessagesRequest getMessagesRequest) {
        token = token.substring(7);
        User user1 = userService.getUserByUsername(jwtUtils.getUserNameFromJwtToken(token));
        User user2 = userService.getUserByUsername(getMessagesRequest.getUsername());
        if (user2 == null) {
            return ResponseEntity.status(400).body(new MessageResponse("Can not find user with this username"));
        }
        long user1Id = user1.getId();
        long user2Id = user2.getId();
        List<Message> messages = messageService.findMessages(user1Id, user2Id);
        return ResponseEntity.ok(new GetMessagesResponse(messages));
    }
}
