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
public class MessageDTO {

    String username;

    String message;

    String messageType;

    Long timestamp;

}
