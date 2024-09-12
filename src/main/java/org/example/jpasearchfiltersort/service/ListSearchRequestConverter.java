package org.example.jpasearchfiltersort.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ListSearchRequestConverter implements Converter<String, BasicSearchRequest> {

    @Override
    public BasicSearchRequest convert(@Nullable String source) {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] valueDecoded = Base64.decodeBase64(source);
        BasicSearchRequest basicSearchRequest;
        try {
            basicSearchRequest = objectMapper.readValue(new String(valueDecoded), BasicSearchRequest.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Не удалость конвертировать base64 в JSON. Ошибка: " + e.getMessage());
        }
        return basicSearchRequest;
    }

}
