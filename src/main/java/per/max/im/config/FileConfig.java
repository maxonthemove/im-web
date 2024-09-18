package per.max.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Desc
 * </p>
 *
 * @date 2024-09-18
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "file")
public class FileConfig {

    private String relativePath;

    private String text;

    private String img;

}
