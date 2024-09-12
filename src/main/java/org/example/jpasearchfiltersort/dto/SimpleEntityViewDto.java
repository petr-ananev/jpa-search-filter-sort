package org.example.jpasearchfiltersort.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.jpasearchfiltersort.dto.SimpleEntityDto.ChangeDt;
import org.example.jpasearchfiltersort.dto.SimpleEntityDto.CreateDt;
import org.example.jpasearchfiltersort.dto.SimpleEntityDto.Id;
import org.example.jpasearchfiltersort.dto.SimpleEntityDto.ObjectName;
import org.example.jpasearchfiltersort.dto.SimpleEntityDto.VersionId;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(setterPrefix = "set")
@EqualsAndHashCode(callSuper = false)
public class SimpleEntityViewDto extends SimpleEntityDto implements Id,
                                                                    VersionId,
                                                                    ChangeDt,
                                                                    CreateDt,
                                                                    ObjectName {

    private String id;

    private String versionId;

    private String objectName;

    private Timestamp changeDt;

    private Timestamp createDt;

}
