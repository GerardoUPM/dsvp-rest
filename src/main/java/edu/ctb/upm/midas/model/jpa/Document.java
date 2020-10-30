package edu.ctb.upm.midas.model.jpa;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by gerardo on 20/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project demo
 * @className Document
 * @see
 */
@Entity
@Table(name = "document", catalog = "edsssdb", schema = "")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d")
        , @NamedQuery(name = "Document.findById", query = "SELECT d FROM Document d WHERE d.documentId = :documentId AND d.date = :date")
        , @NamedQuery(name = "Document.findByDocumentId", query = "SELECT d FROM Document d WHERE d.documentId = :documentId")
        , @NamedQuery(name = "Document.findByDate", query = "SELECT d FROM Document d WHERE d.date = :date")
        , @NamedQuery(name = "Document.findBySourceAndSnapshot", query = "" +
        "SELECT d " +
        "FROM Document d " +
        "INNER JOIN d.hasSources hs " +
        "WHERE d.date = :snapshot " +
        "AND hs.sourceId = :sourceId"
        )
})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Document.findByIdNative",
                query = "SELECT d.document_id, d.date "
                        + "FROM document d WHERE d.document_id = :documentId AND d.date = :date ",
                resultSetMapping="DocumentMapping"

        ),
        @NamedNativeQuery(
                name = "Document.findByIdNativeResultClass",
                query = "SELECT d.document_id, d.date "
                        + "FROM document d WHERE d.document_id = :documentId AND d.date = :date ",
                resultClass = Document.class
        ),
//        @NamedNativeQuery(
//                name = "Document.findLastVersionNative",
//                query = "SELECT MAX(date) 'last' "
//                        + "FROM document d "
//        ),
        @NamedNativeQuery(
                name = "Document.findLastVersionNative",
                query = "select max(d.date) 'last'" +
                        "from document d " +
                        "inner join has_source hs on d.document_id = hs.document_id and d.date = hs.date " +
                        "inner join source s on hs.source_id = s.source_id " +
                        "where s.name = :source "
        ),

        // INSERTS

        @NamedNativeQuery(
                name = "Document.insertNative",
                query = "INSERT INTO document (document_id, date) "
                        + "VALUES (:documentId, :date)"
        ),
        @NamedNativeQuery(
                name = "DocumentUrl.insertNative",
                query = "INSERT IGNORE INTO document_url (document_id, date, url_id) "
                        + "VALUES (:documentId, :date, :urlId)"
        ),


        @NamedNativeQuery(
                name = "Url.insertNative_",
                query = "INSERT INTO url (url_id, url) "
                        + "VALUES (:urlId, :url)"
        ),


        @NamedNativeQuery(
                name = "Source.insertNative_",
                query = "INSERT INTO source (source_id, name) "
                        + "VALUES (:sourceId, :name)"
        ),
        @NamedNativeQuery(
                name = "HasSource.insertNative_",
                query = "INSERT INTO has_source (document_id, date, source_id) "
                        + "VALUES (:documentId, :date, :sourceId)"
        ),

        @NamedNativeQuery(
                name = "SourceUrl.insertNative_",
                query = "INSERT INTO source_url (source_id, url_id) "
                        + "VALUES (:sourceId, :urlId)"
        ),


        @NamedNativeQuery(
                name = "Code.insertNative_",
                query = "INSERT INTO code (code, resource_id) "
                        + "VALUES (:code, :resourceId)"
        ),
        @NamedNativeQuery(
                name = "HasCode.insertNative_",
                query = "INSERT INTO has_code (document_id, date, code, resource_id) "
                        + "VALUES (:documentId, :date, :code, :resourceId)"
        ),
        @NamedNativeQuery(
                name = "CodeUrl.insertNative_",
                query = "INSERT INTO code_url (code, resource_id, url_id) "
                        + "VALUES (:code, :resourceId, :urlId)"
        ),


        @NamedNativeQuery(
                name = "Resource.insertNative_",
                query = "INSERT INTO resource (resource_id, name) "
                        + "VALUES (:resourceId, :name)"
        ),


        @NamedNativeQuery(
                name = "Disease.insertNative_",
                query = "INSERT INTO disease (disease_id, name, cui) "
                        + "VALUES (:diseaseId, :name, :cui)"
        ),
        @NamedNativeQuery(
                name = "HasDisease.insertNative_",
                query = "INSERT INTO has_disease (document_id, date, disease_id) "
                        + "VALUES (:documentId, :date, :diseaseId)"
        ),


        @NamedNativeQuery(
                name = "Section.insertNative_",
                query = "INSERT INTO section (section_id, name, description) "
                        + "VALUES (:sectionId, :name, :description)"
        ),
        @NamedNativeQuery(
                name = "HasSection.insertNative__",
                query = "INSERT INTO has_section (document_id, date, section_id) "
                        + "VALUES (:documentId, :date, :code, :sectionId)"
        ),


        @NamedNativeQuery(
                name = "Text.insertNative_",
                query = "INSERT INTO text (text_id, content_type, text) "
                        + "VALUES (:textId, :contentType, :text)"
        ),
        @NamedNativeQuery(
                name = "HasText.insertNative__",
                query = "INSERT INTO has_text (document_id, date, section_id, text_id, text_order) "
                        + "VALUES (:documentId, :date, :sectionId, :textId, :textOrder)"
        ),
        @NamedNativeQuery(
                name = "TextUrl.insertNative__",
                query = "INSERT INTO text_url (text_id, url_id) "
                        + "VALUES (:textId, :urlId)"
        ),


        @NamedNativeQuery(
                name = "SemanticType.insertNative_",
                query = "INSERT INTO semantic_type (semantic_type, description) "
                        + "VALUES (:semanticType, :description)"
        ),
        @NamedNativeQuery(
                name = "HasSemanticType.insertNative_",
                query = "INSERT INTO has_semantic_type (cui, semantic_type) "
                        + "VALUES (:cui, :semanticType)"
        ),


        @NamedNativeQuery(
                name = "Symptom.insertNative_",
                query = "INSERT INTO symptom (cui, name) "
                        + "VALUES (:cui, :name)"
        ),
        @NamedNativeQuery(
                name = "HasSymptom.insertNative_",
                query = "INSERT INTO has_symptom (text_id, cui, validated) "
                        + "VALUES (:textId, :cui, :validated)"
        ),
        @NamedNativeQuery(
                name = "Document.findAllArticlesAndSnapshot",
                query = "SELECT d.disease_id, d.name, d.snapshot_id, d.actual_snapshot, d.anterior_snapshot " +
                        "FROM new_tbl_disease_list d"
        ),
        @NamedNativeQuery(
                name = "Document.findAllDistinctArticlesAndSnapshot",
                query = "SELECT DISTINCT d.disease_id, d.name " +
                        "FROM new_tbl_disease_list d " +
                        "WHERE d.relevant = 1 "
        ),
        @NamedNativeQuery(
                name = "Document.findAllSnapshotsOfAArticle",
                query = "SELECT d.snapshot_id, d.actual_snapshot, d.anterior_snapshot\n" +
                        "FROM new_tbl_disease_list d\n" +
                        "WHERE d.disease_id = :diseaseId "
        ),
        @NamedNativeQuery(
                name = "Document.getDiseaseDKEInfoToAnalysis",
                query = "SELECT DISTINCT e.disease_id\n" +
                        "     , e.name " +
                        "     , d.snapshot_id " +
                        "     , e.actual_snapshot " +
                        "     , e.anterior_snapshot " +
                        "     , e.dke_count " +
                        "     , e.abs_variation_dke_count " +
                        "     , e.real_variation_dke_count " +
                        "     , e.dke_variation " +
                        "FROM new_tbl_disease_dke_list e " +
                        "INNER JOIN new_tbl_disease_section_list s ON s.disease_id = e.disease_id AND s.actual_snapshot = e.actual_snapshot " +
                        "INNER JOIN new_tbl_disease_url_list u ON u.disease_id = e.disease_id AND u.actual_snapshot = e.actual_snapshot " +
                        "INNER JOIN new_tbl_disease_list d ON d.disease_id = u.disease_id AND d.actual_snapshot = u.actual_snapshot " +
                        "WHERE d.relevant = 1 " +
                        "AND d.disease_id = :diseaseId " +
                        "ORDER BY e.actual_snapshot ASC "
        )

})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "DocumentMapping",
                entities = @EntityResult(
                        entityClass = Document.class,
                        fields = {
                                @FieldResult(name = "documentId", column = "document_id"),
                                @FieldResult(name = "date", column = "date")
                        }
                )
        )
})

@IdClass(DocumentPK.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="documentPK")
public class Document {
    private String documentId;
    private Date date;
    private List<DocumentUrl> documentUrls;
    private List<HasCode> hasCodes;
    private List<HasDisease> hasDiseases;
    private List<HasSection> hasSections;
    private List<HasSource> hasSources;
    private List<DocumentSet> documentSets;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(documentId, document.documentId) &&
                Objects.equals(date, document.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, date);
    }

    @OneToMany(mappedBy = "document")
    public List<DocumentUrl> getDocumentUrls() {
        return documentUrls;
    }

    public void setDocumentUrls(List<DocumentUrl> documentUrls) {
        this.documentUrls = documentUrls;
    }

    @OneToMany(mappedBy = "document")
    public List<HasCode> getHasCodes() {
        return hasCodes;
    }

    public void setHasCodes(List<HasCode> hasCodes) {
        this.hasCodes = hasCodes;
    }

    @OneToMany(mappedBy = "document")
    public List<HasDisease> getHasDiseases() {
        return hasDiseases;
    }

    public void setHasDiseases(List<HasDisease> hasDiseases) {
        this.hasDiseases = hasDiseases;
    }

    @OneToMany(mappedBy = "document")
    public List<HasSection> getHasSections() {
        return hasSections;
    }

    public void setHasSections(List<HasSection> hasSections) {
        this.hasSections = hasSections;
    }

    @OneToMany(mappedBy = "document")
    public List<HasSource> getHasSources() {
        return hasSources;
    }

    public void setHasSources(List<HasSource> hasSources) {
        this.hasSources = hasSources;
    }

    @OneToMany(mappedBy = "document")
    public List<DocumentSet> getDocumentSets() {
        return documentSets;
    }

    public void setDocumentSets(List<DocumentSet> documentSets) {
        this.documentSets = documentSets;
    }


}
