package per.max.im.controller;

import org.springframework.web.bind.annotation.*;
import per.max.im.domain.MessageDTO;
import per.max.im.domain.SendMessageRequest;
import per.max.im.service.MessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * Desc
 * </p>
 *
 * @date 2024-09-18
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private MessageService messageService;

    @GetMapping("/getMessage")
    public List<MessageDTO> getMessage(@RequestParam String channel, HttpServletRequest request) {
        return messageService.getMessage(channel);
    }


    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody SendMessageRequest sendMessageRequest, HttpServletRequest request) {
        messageService.sendMessage(sendMessageRequest, request);
    }


    @GetMapping("/clear")
    public String clear() {
        messageService.clear();
        return "success";
    }
}
