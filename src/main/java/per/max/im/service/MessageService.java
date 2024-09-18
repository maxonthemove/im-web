package per.max.im.service;

import per.max.im.domain.MessageDTO;
import per.max.im.domain.SendMessageRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * Desc
 * </p>
 *
 * @author wangdongjie@xiaomi.com
 * @date 2024-09-18
 */
public interface MessageService {

    /**
     * <p>
     * Desc
     * </p>
     *
     * @param channel
     * @return
     */
    List<MessageDTO> getMessage(String channel);

    /**
     * <p>
     * Desc
     * </p>
     *
     * @param sendMessageRequest
     */
    void sendMessage(SendMessageRequest sendMessageRequest, HttpServletRequest request);

    void clear();

}
