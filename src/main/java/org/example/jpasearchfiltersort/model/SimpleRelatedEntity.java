package org.example.jpasearchfiltersort.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "simple_related_entity")
public class SimpleRelatedEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "obj_id", nullable = false, length = 36)
    private String objectId;

    @Column(name = "obj_name", nullable = false, length = 100)
    private String objectName;

}
