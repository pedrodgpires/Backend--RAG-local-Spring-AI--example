package pp.ai.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.ai.demo.dto.MessageRequestDto;
import pp.ai.demo.dto.MessageResponseDto;
import pp.ai.demo.services.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;


    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * This method is used to send a message to the chat model
     * @param messageRequestDto The message to send
     * @return The response from the chat model
     */
    @PostMapping("/ask")
    public ResponseEntity<MessageResponseDto> askChat (@RequestBody MessageRequestDto messageRequestDto) {
        try {
            MessageResponseDto response = chatService.sendMessage(messageRequestDto);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
