package per.max.im.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
@ConfigurationProperties(prefix = "file")
@Data
public class FileConfig {

    @Value("${relative-path}")
    private String relativePath;

    @Value("${text}")
    private String text;

    @Value("${img}")
    private String img;


}
