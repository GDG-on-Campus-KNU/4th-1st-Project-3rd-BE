package GDG4th.personaChat.global.configurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        ObjectMapper snakeCaseMapper = new ObjectMapper();
        snakeCaseMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        snakeCaseMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ObjectMapper camelCaseMapper = new ObjectMapper();
        camelCaseMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        camelCaseMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 요청 및 응답 모두 처리 가능한 커스텀 HttpMessageConverter 구성
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(snakeCaseMapper) {
            @Override
            protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
                    throws IOException, HttpMessageNotReadableException {
                return camelCaseMapper.readValue(inputMessage.getBody(), clazz);
            }
        };

        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));

        return new RestTemplate(List.of(converter));
    }
}
