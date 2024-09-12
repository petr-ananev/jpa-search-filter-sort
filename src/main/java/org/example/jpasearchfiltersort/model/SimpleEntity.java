package org.example.jpasearchfiltersort.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "simple_entity")
@NamedEntityGraph(name = "simple-graph",
                  attributeNodes = {
                          @NamedAttributeNode(value = "simpleRelatedEntity")
                  }
)
public class SimpleEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @UuidGenerator
    @Column(name = "version_id", nullable = false, unique = true)
    private String version_id;

    @Basic
    @CreationTimestamp
    @Column(name = "create_dt", updatable = false)
    private Timestamp createDt;

    @Basic
    @UpdateTimestamp
    @Column(name = "change_dt")
    private Timestamp changeDt;

    @Column(name = "id", length = 36, nullable = false, insertable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id",
                referencedColumnName = "obj_id",
                nullable = false)
    private SimpleRelatedEntity simpleRelatedEntity;

}
