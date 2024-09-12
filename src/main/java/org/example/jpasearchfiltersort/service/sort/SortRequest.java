package org.example.jpasearchfiltersort.service.sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

/**
 * Объект сортировки. Прилетает заполненный с клиента.
 * <p>
 * Состоит из:
 * Направление сортировки (asc,desc) {@link #getDirection()}
 * Колонка сортировки {@link #getColumnName()}
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortRequest {

    Direction direction;

    String columnName;

}
