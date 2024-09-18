package per.max.im.controller;

import org.springframework.web.bind.annotation.*;
import per.max.im.domain.MessageDTO;
import per.max.im.domain.SendMessageRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<String, List<MessageDTO>> messageMap = new ConcurrentHashMap<>();


    @GetMapping("/getMessage")
    public List<MessageDTO> getMessage(@RequestParam String channel, HttpServletRequest request) {
        List<MessageDTO> messageDTOS = messageMap.get(channel);
        if (messageDTOS == null) {
            messageDTOS = new LinkedList<>();
            messageMap.put(channel, messageDTOS);
        }
        return messageMap.get(channel);
    }


    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody SendMessageRequest sendMessageRequest, HttpServletRequest request) {
        List<MessageDTO> messageDTOS = messageMap.get(sendMessageRequest.getChannel());
        if (messageDTOS == null) {
            messageDTOS = new LinkedList<>();
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(sendMessageRequest.getMessage());
        messageDTO.setUsername(getCookie(request, "username"));
        messageDTO.setTimestamp(System.currentTimeMillis());
        messageDTOS.add(messageDTO);
        messageMap.put(sendMessageRequest.getChannel(), messageDTOS);
    }

    private String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @GetMapping("/clear")
    public String clear() {
        messageMap.clear();
        return "success";
    }
}
