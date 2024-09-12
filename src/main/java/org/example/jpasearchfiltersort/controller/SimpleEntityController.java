package org.example.jpasearchfiltersort.controller;

import lombok.RequiredArgsConstructor;
import org.example.jpasearchfiltersort.dto.SimpleEntityViewDto;
import org.example.jpasearchfiltersort.model.PageDto;
import org.example.jpasearchfiltersort.service.BasicSearchRequest;
import org.example.jpasearchfiltersort.service.SimpleEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class SimpleEntityController implements SimpleEntityApi {

    private final SimpleEntityService simpleEntityService;

    @Override
    public ResponseEntity<PageDto<SimpleEntityViewDto>> getAll(BasicSearchRequest searchRequest) {
        return new ResponseEntity<>(simpleEntityService.getAll(searchRequest), OK);
    }

}
