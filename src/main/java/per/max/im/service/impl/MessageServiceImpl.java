package per.max.im.service.impl;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import per.max.im.config.FileConfig;
import per.max.im.domain.MessageDTO;
import per.max.im.domain.SendMessageRequest;
import per.max.im.service.MessageService;
import per.max.im.util.JacksonUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static per.max.im.util.RequestUtil.getCookie;

/**
 * <p>
 * Desc
 * </p>
 *
 * @author wangdongjie@xiaomi.com
 * @date 2024-09-18
 */
@Service
public class MessageServiceImpl implements MessageService {

    private Map<String, List<MessageDTO>> messageMap = new ConcurrentHashMap<>();

    @Override
    public List<MessageDTO> getMessage(String channel) {
        if (StringUtils.isBlank(channel)) {
            channel = "default";
        }
        if (messageMap.isEmpty()) {
            String path = fileConfig.getRelativePath() + fileConfig.getText();
            FileReader file = new FileReader(path);
            String jsonString = file.readString();
            if (StringUtils.isNotBlank(jsonString)) {
                messageMap = JacksonUtil.parseObject(jsonString, ConcurrentHashMap.class);
            }
        }
        List<MessageDTO> messageDTOS = messageMap.get(channel);
        if (messageDTOS == null) {
            messageDTOS = new LinkedList<>();
            messageMap.put(channel, messageDTOS);
        }
        return messageMap.get(channel);
    }

    @Override
    public void sendMessage(SendMessageRequest sendMessageRequest, HttpServletRequest request) {
        if (StringUtils.isBlank(sendMessageRequest.getMessage())) {
            return;
        }
        if (StringUtils.isBlank(sendMessageRequest.getChannel())) {
            sendMessageRequest.setChannel("default");
        }
        if (StringUtils.isBlank(sendMessageRequest.getMessageType())) {
            sendMessageRequest.setMessageType("text");
        }
        List<MessageDTO> messageDTOS = messageMap.get(sendMessageRequest.getChannel());
        if (messageDTOS == null) {
            messageDTOS = new LinkedList<>();
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(sendMessageRequest.getMessage());
        messageDTO.setUsername(getCookie(request, "username"));
        messageDTO.setTimestamp(System.currentTimeMillis());
        messageDTO.setMessageType(sendMessageRequest.getMessageType());
        messageDTOS.add(messageDTO);
        messageMap.put(sendMessageRequest.getChannel(), messageDTOS);

        writeMessageToFile(JacksonUtil.toJsonString(messageMap));
    }

    @Resource
    private FileConfig fileConfig;

    private void writeMessageToFile(String jsonString) {
        String path = fileConfig.getRelativePath() + fileConfig.getText();
        //文件不存在则创建
        FileWriter writer = new FileWriter(path);
        writer.write(jsonString);
    }

    @Override
    public void clear() {
        messageMap.clear();
    }


}
