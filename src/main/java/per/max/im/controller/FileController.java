package per.max.im.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/upload")
    public String upload(){
        return "upload";
    }


}
