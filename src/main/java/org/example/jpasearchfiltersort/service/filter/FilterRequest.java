package org.example.jpasearchfiltersort.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jpasearchfiltersort.enums.Operator;

import java.util.List;

/**
 * Объект фильтрации. Прилетает заполненный с клиента.
 * <p>
 * Состоит из:
 * Колонка к которой применяется фильтрация {@link #getColumnName()}
 * Оператор фильтрации (LIKE,EQUAL etc) {@link #getOperator()}
 * Значение фильтрации {@link #getValue()}
 * Значение фильтрации правой границы (для between) {@link #getValueTo()}
 * Коллеция значений (для in)  {@link #getValues()}
 * </p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

    private String columnName;

    private Operator operator;

    private Object value;

    private Object valueTo;

    private List<Object> values;

}
