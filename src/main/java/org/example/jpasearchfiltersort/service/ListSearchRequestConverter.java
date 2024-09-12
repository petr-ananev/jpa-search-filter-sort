package com.glowbyte.decision.core.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glowbyte.decision.core.model.search.ListSearchRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ListSearchRequestConverter implements Converter<String, ListSearchRequest> {

    @Override
    public ListSearchRequest convert(@Nullable String source) {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] valueDecoded = Base64.decodeBase64(source);
        ListSearchRequest listSearchRequest;
        try {
            listSearchRequest = objectMapper.readValue(new String(valueDecoded), ListSearchRequest.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Не удалость конвертировать base64 в JSON. Ошибка: " + e.getMessage());
        }
        return listSearchRequest;
    }

}
