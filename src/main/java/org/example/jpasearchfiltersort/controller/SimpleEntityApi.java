package org.example.jpasearchfiltersort.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.jpasearchfiltersort.dto.SimpleEntityPage;
import org.example.jpasearchfiltersort.dto.SimpleEntityViewDto;
import org.example.jpasearchfiltersort.model.PageDto;
import org.example.jpasearchfiltersort.service.BasicSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.DEFAULT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RequestMapping("/simple")
@Tag(name = "SimpleEntity API")
public interface SimpleEntityApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Успешное получение полного списка",
                         content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = SimpleEntityPage.class)))
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<PageDto<SimpleEntityViewDto>> getAll(@Parameter(in = DEFAULT,
                                                                   description = "Объект для задания параметров фильтра",
                                                                   example = "ewoJImZpbHRlcnMiOiBbCgkJewoJCQkiY29sdW1uTmFtZSI6ICJkaXNwbGF5TmFtZSIsCgkJCSJvcGVyYXRvciI6ICJFUVVBTCIsCgkJCSJ2YWx1ZSI6ICJ0ZXN0IgoJCX0KCV0sCgkic29ydHMiOiBbCgkJewoJCQkiZGlyZWN0aW9uIjogIkFTQyIsCgkJCSJjb2x1bW5OYW1lIjogImRpc3BsYXlOYW1lIgoJCX0KCV0sCgkic2VhcmNoQnkiOiAi0KHRgtGA0L7QutCwINC/0L7QuNGB0LrQsCIsCgkicGFnZSI6IDEsCgkic2l6ZSI6IDEwCn0=",
                                                                   schema = @Schema(implementation = String.class))
                                                            @RequestParam(required = false)
                                                            BasicSearchRequest searchRequest);

}
