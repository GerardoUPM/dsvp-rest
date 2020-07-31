package edu.ctb.upm.midas.model.jpa;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

/**
 * Created by gerardo on 20/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project demo
 * @className SemanticType
 * @see
 */
@Entity
@Table(name = "semantic_type", catalog = "edsssdb", schema = "")
@SQLInsert(sql = "INSERT IGNORE INTO semantic_type (semantic_type, description) " +
        "VALUES (?, ?)" )
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "SemanticType.findAll", query = "SELECT s FROM SemanticType s")
        , @NamedQuery(name = "SemanticType.findById", query = "SELECT s FROM SemanticType s WHERE s.semanticType = :semanticType")
        , @NamedQuery(name = "SemanticType.findBySemanticType", query = "SELECT s FROM SemanticType s WHERE s.semanticType = :semanticType")
        , @NamedQuery(name = "SemanticType.findByDescription", query = "SELECT s FROM SemanticType s WHERE s.description = :description")

})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "SemanticType.findByIdNative",
                query = "SELECT s.semantic_type, s.description "
                        + "FROM semantic_type s WHERE s.semantic_type = :semanticType",
                resultSetMapping="SemanticTypeMapping"

        ),
        @NamedNativeQuery(
                name = "SemanticType.findByIdNativeResultClass",
                query = "SELECT s.semantic_type, s.description "
                        + "FROM semantic_type s WHERE s.semantic_type = :semanticType",
                resultClass = SemanticType.class
        ),


        @NamedNativeQuery(
                name = "SemanticType.insertNative",
                query = "INSERT INTO semantic_type (semantic_type, description) "
                        + "VALUES (:semanticType, :description)"
        ),
        @NamedNativeQuery(
                name = "HasSemanticType.insertNative",
                query = "INSERT INTO has_semantic_type (cui, semantic_type) "
                        + "VALUES (:cui, :semanticType)"
        )
})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "SemanticTypeMapping",
                entities = @EntityResult(
                        entityClass = SemanticType.class,
                        fields = {
                                @FieldResult(name = "semanticType", column = "semantic_type"),
                                @FieldResult(name = "description", column = "description")
                        }
                )
        )
})

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="semanticType")
public class SemanticType {
    private String semanticType;
    private String description;
    private List<HasSemanticType> hasSemanticTypesBySemanticType;

    public SemanticType() {
    }

    public SemanticType(String semanticType) {
        this.semanticType = semanticType;
    }

    @Id
    @Column(name = "semantic_type", nullable = false, length = 8)
    public String getSemanticType() {
        return semanticType;
    }

    public void setSemanticType(String semanticType) {
        this.semanticType = semanticType;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 50)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemanticType that = (SemanticType) o;
        return Objects.equals(semanticType, that.semanticType) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(semanticType, description);
    }

    @OneToMany(mappedBy = "semanticTypeBySemanticType")
    public List<HasSemanticType> getHasSemanticTypesBySemanticType() {
        return hasSemanticTypesBySemanticType;
    }

    public void setHasSemanticTypesBySemanticType(List<HasSemanticType> hasSemanticTypesBySemanticType) {
        this.hasSemanticTypesBySemanticType = hasSemanticTypesBySemanticType;
    }
}
