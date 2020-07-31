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
 * @className HasSymptom
 * @see
 */
@Entity
@Table(name = "has_symptom", catalog = "edsssdb", schema = "")
@SQLInsert(sql = "INSERT IGNORE INTO has_symptom (text_id, cui, validated, matched_words, positional_info) " +
        "VALUES (?, ?, ?, ?, ?)" )
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "HasSymptom.findAll", query = "SELECT h FROM HasSymptom h")
        , @NamedQuery(name = "HasSymptom.findById", query = "SELECT h FROM HasSymptom h WHERE h.textId = :textId AND h.cui = :cui")
        , @NamedQuery(name = "HasSymptom.findByTextId", query = "SELECT h FROM HasSymptom h WHERE h.textId = :textId")
        , @NamedQuery(name = "HasSymptom.findByCui", query = "SELECT h FROM HasSymptom h WHERE h.cui = :cui")
        , @NamedQuery(name = "HasSymptom.findByValidated", query = "SELECT h FROM HasSymptom h WHERE h.validated = :validated")

})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "HasSymptom.findByIdNative",
                query = "SELECT h.text_id, h.cui, h.validated, h.matched_words, h.positional_info "
                        + "FROM has_symptom h WHERE h.text_id = :textId AND h.cui = :cui"//,
                //resultSetMapping="HasSymptomMapping"

        ),
        @NamedNativeQuery(
                name = "HasSymptom.findByIdNativeResultClass",
                query = "SELECT h.text_id, h.cui, h.validated, h.matched_words, h.positional_info "
                        + "FROM has_symptom h WHERE h.text_id = :textId AND h.cui = :cui",
                resultClass = HasSymptom.class
        ),


        @NamedNativeQuery(
                name = "HasSymptom.insertNative",
                query = "INSERT INTO has_symptom (text_id, cui, validated, matched_words, positional_info) "
                        + "VALUES (:textId, :cui, :validated, :matchedWords, :positionalInfo)"
        ),


        @NamedNativeQuery(
                name = "HasSymptom.updateValidatedNative",
                query = "UPDATE has_symptom h " +
                        "SET h.validated = :validated " +
                        "WHERE h.text_id LIKE :version " +
                        "AND h.text_id LIKE :sourceId " +
                        "AND h.cui = :cui "
        ),

        @NamedNativeQuery(
                name = "HasSymptom.updateMatchedWordsAndPositionalInfoNative",
                query = "UPDATE has_symptom h " +
                        "SET h.matched_words = :matchedWords, " +
                        "h.positional_info = :positionalInfo " +
                        "WHERE h.text_id LIKE :textId " +
                        "AND h.cui = :cui "
        )


})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "HasSymptomMapping",
                entities = @EntityResult(
                        entityClass = HasSymptom.class,
                        fields = {
                                @FieldResult(name = "textId", column = "text"),
                                @FieldResult(name = "cui", column = "symptom"),
                                @FieldResult(name = "validated", column = "validated"),
                                @FieldResult(name = "matched_words", column = "matchedWords"),
                                @FieldResult(name = "positional_info", column = "positionalInfo")
                        }
                )
        )
})

@IdClass(HasSymptomPK.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="hasSymptomPK")
public class HasSymptom {
    private String textId;
    private String cui;
    private Byte validated;
    private String matchedWords;
    private String positionalInfo;
    private Text textByTextId;
    private Symptom symptomByCui;

    public HasSymptom() {
    }

    public HasSymptom(String textId, String cui, Byte validated) {
        this.textId = textId;
        this.cui = cui;
        this.validated = validated;
    }

    @Id
    @Column(name = "text_id", nullable = false, length = 255)
    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    @Id
    @Column(name = "cui", nullable = false, length = 8)
    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    @Basic
    @Column(name = "validated", nullable = false)
    public Byte getValidated() {
        return validated;
    }

    public void setValidated(Byte validated) {
        this.validated = validated;
    }

    @Basic
    @Column(name = "matched_words", nullable = false)
    public String getMatchedWords() {
        return matchedWords;
    }

    public void setMatchedWords(String matchedWords) {
        this.matchedWords = matchedWords;
    }

    @Basic
    @Column(name = "positional_info", nullable = false)
    public String getPositionalInfo() {
        return positionalInfo;
    }

    public void setPositionalInfo(String positionalInfo) {
        this.positionalInfo = positionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HasSymptom that = (HasSymptom) o;
        return Objects.equals(textId, that.textId) &&
                Objects.equals(cui, that.cui) &&
                Objects.equals(validated, that.validated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textId, cui, validated, matchedWords, positionalInfo);
    }

    @ManyToOne
    @JoinColumn(name = "text_id", referencedColumnName = "text_id", nullable = false, insertable = false, updatable = false)
    public Text getTextByTextId() {
        return textByTextId;
    }

    public void setTextByTextId(Text textByTextId) {
        this.textByTextId = textByTextId;
    }

    @ManyToOne
    @JoinColumn(name = "cui", referencedColumnName = "cui", nullable = false, insertable = false, updatable = false)
    public Symptom getSymptomByCui() {
        return symptomByCui;
    }

    public void setSymptomByCui(Symptom symptomByCui) {
        this.symptomByCui = symptomByCui;
    }

}
