package per.max.im.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import per.max.im.config.FileConfig;
import per.max.im.domain.SendMessageRequest;
import per.max.im.service.MessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static per.max.im.util.RequestUtil.getCookie;

/**
 * <p>
 * Desc
 * </p>
 *
 * @date 2024-09-18
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileConfig fileConfig;

    @Resource
    private MessageService messageService;

    @PostMapping("/uploadImg")
    public String uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (!file.isEmpty()) {
            try {
                // 这里只是简单例子，文件直接输出到项目路径下。
                // 实际项目中，文件需要输出到指定位置，需要在增加代码处理。
                // 还有关于文件格式限制、文件大小限制，详见：中配置。
                String[] split = file.getOriginalFilename().split("\\.");
                String suffix = split[split.length - 1];
                String filePath = fileConfig.getImg() + getCookie(request, "username") + "-" + System.currentTimeMillis() + "." + suffix;
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileConfig.getRelativePath() + filePath));
                out.write(file.getBytes());
                out.flush();
                out.close();
                SendMessageRequest sendMessageRequest = new SendMessageRequest();
                sendMessageRequest.setMessageType("img");
                sendMessageRequest.setMessage("./file/" + filePath);
                messageService.sendMessage(sendMessageRequest, request);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "上传成功";
        } else {
            return "上传失败，因为文件是空的.";
        }
    }


}
