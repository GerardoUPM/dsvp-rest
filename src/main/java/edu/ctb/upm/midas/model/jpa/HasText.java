package edu.ctb.upm.midas.model.jpa;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.util.Objects;

/**
 * Created by gerardo on 20/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project demo
 * @className HasText
 * @see
 */
@Entity
@Table(name = "has_text", catalog = "edsssdb", schema = "")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "HasText.findAll", query = "SELECT h FROM HasText h")
        , @NamedQuery(name = "HasText.findById", query = "SELECT h FROM HasText h WHERE h.documentId = :documentId AND h.date = :date AND h.sectionId = :sectionId AND h.textId = :textId")
        , @NamedQuery(name = "HasText.findByDocumentId", query = "SELECT h FROM HasText h WHERE h.documentId = :documentId")
        , @NamedQuery(name = "HasText.findByDate", query = "SELECT h FROM HasText h WHERE h.date = :date")
        , @NamedQuery(name = "HasText.findBySectionId", query = "SELECT h FROM HasText h WHERE h.sectionId = :sectionId")
        , @NamedQuery(name = "HasText.findByTextId", query = "SELECT h FROM HasText h WHERE h.textId = :textId")
        , @NamedQuery(name = "HasText.findByTextOrder", query = "SELECT h FROM HasText h WHERE h.textOrder = :textOrder")
        , @NamedQuery(name = "HasText.updateById", query = "UPDATE HasText h SET h.textOrder = :textOrder WHERE h.documentId = :documentId AND h.date = :date AND h.sectionId = :sectionId AND h.textId = :textId")
})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "HasText.findByIdNative",
                query = "SELECT h.document_id, h.date, h.section_id, h.text_id, h.text_order "
                        + "FROM has_text h WHERE h.document_id = :documentId AND h.date = :date AND h.section_id = :sectionId AND h.text_id = :textId",
                resultSetMapping="HasTextMapping"

        ),
        @NamedNativeQuery(
                name = "HasText.findByIdNativeResultClass",
                query = "SELECT h.document_id, h.date, h.section_id, h.text_id, h.text_order "
                        + "FROM has_text h WHERE h.document_id = :documentId AND h.date = :date AND h.section_id = :sectionId AND h.text_id = :textId",
                resultClass = HasText.class
        )

        ,
        @NamedNativeQuery(
                name = "HasText.insertNative",
                query = "INSERT IGNORE INTO has_text (document_id, date, section_id, text_id, text_order) "
                        + "VALUES (:documentId, :date, :sectionId, :textId, :textOrder)"
        ),
        @NamedNativeQuery(
                name = "HasText.associated",
                query = "select count(*) from has_text where date = :source and text_id like :sourceCode "
        )
})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "HasTextMapping",
                entities = @EntityResult(
                        entityClass = HasText.class,
                        fields = {
                                @FieldResult(name = "documentId", column = "document_id"),
                                @FieldResult(name = "date", column = "date"),
                                @FieldResult(name = "sectionId", column = "section_id"),
                                @FieldResult(name = "textId", column = "text_id"),
                                @FieldResult(name = "textOrder", column = "text_order")
                        }
                )
        )
})

@IdClass(HasTextPK.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="hasTextPK")
public class HasText {
    private String documentId;
    private Date date;
    private String sectionId;
    private String textId;
    private Integer textOrder;
    private HasSection hasSection;
    private Text textByTextId;

    @Id
    @Column(name = "document_id", nullable = false, length = 30)
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Id
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Id
    @Column(name = "section_id", nullable = false, length = 10)
    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @Id
    @Column(name = "text_id", nullable = false, length = 255)
    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    @Basic
    @Column(name = "text_order", nullable = false)
    public Integer getTextOrder() {
        return textOrder;
    }

    public void setTextOrder(Integer textOrder) {
        this.textOrder = textOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HasText hasText = (HasText) o;
        return Objects.equals(documentId, hasText.documentId) &&
                Objects.equals(date, hasText.date) &&
                Objects.equals(sectionId, hasText.sectionId) &&
                Objects.equals(textId, hasText.textId) &&
                Objects.equals(textOrder, hasText.textOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, date, sectionId, textId, textOrder);
    }

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "document_id", referencedColumnName = "document_id", nullable = false, insertable = false, updatable = false), @JoinColumn(name = "date", referencedColumnName = "date", nullable = false, insertable = false, updatable = false), @JoinColumn(name = "section_id", referencedColumnName = "section_id", nullable = false, insertable = false, updatable = false)})
    public HasSection getHasSection() {
        return hasSection;
    }

    public void setHasSection(HasSection hasSection) {
        this.hasSection = hasSection;
    }

    @ManyToOne
    @JoinColumn(name = "text_id", referencedColumnName = "text_id", nullable = false, insertable = false, updatable = false)
    public Text getTextByTextId() {
        return textByTextId;
    }

    public void setTextByTextId(Text textByTextId) {
        this.textByTextId = textByTextId;
    }

}
