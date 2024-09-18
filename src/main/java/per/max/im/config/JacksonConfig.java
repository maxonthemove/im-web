package per.max.im.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * Desc
 * </p>
 *
 * @date 2022-07-04
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        final JsonMapper.Builder mapper = JsonMapper.builder();

        mapper.configure(MapperFeature.USE_STD_BEAN_NAMING, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.serializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

        // Date
        mapper.defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // LocalDate
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(
                LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(
                LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        // LocalDateTime
        javaTimeModule.addSerializer(
                LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(
                LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        mapper.addModule(javaTimeModule);

        // 声明自定义模块,配置double类型序列化配置
        SimpleModule module = new SimpleModule("DoubleSerializer", new Version(1, 0, 0, "", "", ""));
        // 注意Double和double需要分配配置
        module.addSerializer(Double.class, new DoubleSerializer());
        module.addSerializer(double.class, new DoubleSerializer());
        mapper.addModule(module);

        //// 自动查找并注册Java 8相关模块
        mapper.findAndAddModules();
        return mapper.build();
    }

    static class DoubleSerializer extends JsonSerializer<Double> {

        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            BigDecimal d = new BigDecimal(value.toString());
            gen.writeNumber(d.stripTrailingZeros());
        }

        @Override
        public Class<Double> handledType() {
            return Double.class;
        }
    }
}
