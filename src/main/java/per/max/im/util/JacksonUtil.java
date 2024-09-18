package per.max.im.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Desc
 * </p>
 *
 * @date 2022-07-04
 */
@Slf4j
@Component
public class JacksonUtil {

    private static ObjectMapper mapper = null;

    private JacksonUtil(ObjectMapper mapper) {
        JacksonUtil.mapper = mapper;
    }

    /**
     * 序列化为JSON字符串
     *
     * @param obj obj
     * @return {@link String}
     */
    public static String toJsonString(Object obj) {
        if (obj == null) return null;
        String result = null;
        try {
            result = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    /**
     * 序列化为JSON字符串，格式化
     *
     * @param obj obj
     * @return {@link String}
     */
    public static String toJsonStringPretty(Object obj) {
        if (obj == null) return null;
        String result = null;
        try {
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }


    /**
     * 解析对象
     * 反序列化为Object
     *
     * @param clazz   clazz
     * @param jsonStr json str
     * @return {@link T}
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(jsonStr) || clazz == null) return null;
        T t = null;
        try {
            t = mapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return t;
    }


    /**
     * 解析List
     * 反序列化为List集合
     *
     * @param clazz       clazz
     * @param listJsonStr 列表json str
     * @return {@link List}<{@link T}>
     */
    public static <T> List<T> parseList(String listJsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(listJsonStr) || clazz == null) return Collections.emptyList();
        List<T> list = Collections.emptyList();
        try {
            list = mapper.readValue(listJsonStr, getCollectionType(mapper, List.class, clazz));
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return list;
    }

    public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    /**
     * 解析Map
     * 反序列化为Map集合
     *
     * @param mapJsonStr json str
     * @param kClazz     k clazz
     * @param vClazz     v clazz
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> parseMap(String mapJsonStr, Class<K> kClazz, Class<V> vClazz) {
        if (StringUtils.isBlank(mapJsonStr) || kClazz == null || vClazz == null) return Collections.emptyMap();
        Map<K, V> map = Collections.EMPTY_MAP;
        try {
            map = mapper.readValue(mapJsonStr, mapper.getTypeFactory().constructParametricType(Map.class, kClazz, vClazz));
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return map;
    }

    public static Map<String, Object> parseMap(String mapJsonStr) {
        return parseMap(mapJsonStr, String.class, Object.class);
    }

}
