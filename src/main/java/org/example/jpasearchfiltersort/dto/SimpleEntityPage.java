package org.example.jpasearchfiltersort.dto;

import org.example.jpasearchfiltersort.model.PageDto;
import org.springframework.data.domain.Page;

/**
 * Hack для описание Swagger
 */
public class SimpleEntityPage extends PageDto<SimpleEntityViewDto> {

    public SimpleEntityPage(Page<SimpleEntityViewDto> page) {
        super(page);
    }

}
