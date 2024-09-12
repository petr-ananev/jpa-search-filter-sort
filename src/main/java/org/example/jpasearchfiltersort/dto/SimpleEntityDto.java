package org.example.jpasearchfiltersort.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@SuperBuilder
@NoArgsConstructor
public class SimpleEntityDto {

    interface Id {

        @Schema(type = "string", format = "uuid", maxLength = 36,
                description = "Идентификатор в формате UUID 4",
                example = "a190d64d-ef84-4d17-b05b-096f4b82365a")
        String getId();

    }

    interface VersionId {

        @Schema(type = "string", format = "uuid", maxLength = 36,
                description = "Идентификатор в формате UUID 4",
                example = "a190d64d-ef84-4d17-b05b-096f4b82365a")
        String getVersionId();

    }

    public interface ChangeDt {

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Schema(example = "2021-12-20 21:21:21.333", format = "date-time",
                description = "Дата Время обновления")
        Timestamp getChangeDt();

    }

    public interface CreateDt {

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Schema(example = "2021-12-20 21:21:21.333", format = "date-time",
                description = "Дата Время обновления")
        Timestamp getCreateDt();

    }

    public interface ObjectName {

        @NotBlank(message = "objectName - обязательное поле. Не должно содержать одни пробелы или быть пустым.")
        @Size(max = 100, message = "Имя объекта должно содержать не более 100 символов")
        @Schema(example = "Объект №1", description = "Имя объекта")
        String getObjectName();

    }
}
