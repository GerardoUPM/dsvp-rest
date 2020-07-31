package edu.ctb.upm.midas.model.jpa;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;


/**
 * Created by gerardo on 20/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project demo
 * @className HasSemanticType
 * @see
 */
@Entity
@Table(name = "has_semantic_type", schema = "edsssdb", catalog = "")
//@SQLInsert(sql = "INSERT IGNORE INTO has_semantic_type (cui, semantic_type) " +
//        "VALUES (?, ?)" )
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "HasSemanticType.findAll", query = "SELECT h FROM HasSemanticType h")
        , @NamedQuery(name = "HasSemanticType.findById", query = "SELECT h FROM HasSemanticType h WHERE h.cui = :cui AND h.semanticType = :semanticType")
        , @NamedQuery(name = "HasSemanticType.findByCui", query = "SELECT h FROM HasSemanticType h WHERE h.cui = :cui")
        , @NamedQuery(name = "HasSemanticType.findBySemanticType", query = "SELECT h FROM HasSemanticType h WHERE h.semanticType = :semanticType")

})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "HasSemanticTypeMapping",
                entities = @EntityResult(
                        entityClass = HasSemanticType.class,
                        fields = {
                                @FieldResult(name = "cui", column = "cui"),
                                @FieldResult(name = "semantic_type", column = "semanticType")
                        }
                )
        )
})

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="HasSemanticTypePK")
@IdClass(HasSemanticTypePK.class)
public class HasSemanticType {
    private String cui;
    private String semanticType;
    private Symptom symptomByCui;
    private SemanticType semanticTypeBySemanticType;

    public HasSemanticType() {
    }

    public HasSemanticType(String cui, String semanticType) {
        this.cui = cui;
        this.semanticType = semanticType;
    }

    @Id
    @Column(name = "cui", nullable = false, length = 8)
    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    @Id
    @Column(name = "semantic_type", nullable = false, length = 8)
    public String getSemanticType() {
        return semanticType;
    }

    public void setSemanticType(String semanticType) {
        this.semanticType = semanticType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HasSemanticType that = (HasSemanticType) o;
        return Objects.equals(cui, that.cui) &&
                Objects.equals(semanticType, that.semanticType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cui, semanticType);
    }

    @ManyToOne
    @JoinColumn(name = "cui", referencedColumnName = "cui", nullable = false, insertable = false, updatable = false)
    public Symptom getSymptomByCui() {
        return symptomByCui;
    }

    public void setSymptomByCui(Symptom symptomByCui) {
        this.symptomByCui = symptomByCui;
    }

    @ManyToOne
    @JoinColumn(name = "semantic_type", referencedColumnName = "semantic_type", nullable = false, insertable = false, updatable = false)
    public SemanticType getSemanticTypeBySemanticType() {
        return semanticTypeBySemanticType;
    }

    public void setSemanticTypeBySemanticType(SemanticType semanticTypeBySemanticType) {
        this.semanticTypeBySemanticType = semanticTypeBySemanticType;
    }
}
