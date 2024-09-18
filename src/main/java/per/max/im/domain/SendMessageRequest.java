package per.max.im.domain;

import lombok.Data;

/**
 * <p>
 * Desc
 * </p>
 *
 * @date 2024-09-18
 */
@Data
public class SendMessageRequest {

    private String channel;

    private String message;
}
