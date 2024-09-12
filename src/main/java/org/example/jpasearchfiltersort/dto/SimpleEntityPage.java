package com.glowbyte.decision.complextype.dto;

 import com.glowbyte.decision.core.model.search.DecisionPage;
import org.springframework.data.domain.Page;

/**
 * Hack для описание Swagger
 */
public class ComplexTypePage extends DecisionPage<ComplexTypeGetFullView> {

    public ComplexTypePage(Page<ComplexTypeGetFullView> page) {
        super(page);
    }

}
