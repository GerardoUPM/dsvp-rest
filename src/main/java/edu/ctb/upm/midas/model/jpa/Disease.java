package edu.ctb.upm.midas.model.jpa;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
 * @className Disease
 * @see
 */
@Entity
@Table(name = "disease", catalog = "edsssdb", schema = "")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Disease.findAll", query = "SELECT d FROM Disease d")
        , @NamedQuery(name = "Disease.findById", query = "SELECT d FROM Disease d WHERE d.diseaseId = :diseaseId")
        , @NamedQuery(name = "Disease.findByDiseaseId", query = "SELECT d FROM Disease d WHERE d.diseaseId = :diseaseId")
        , @NamedQuery(name = "Disease.findByName", query = "SELECT d FROM Disease d WHERE d.name = :name")
        , @NamedQuery(name = "Disease.findByCui", query = "SELECT d FROM Disease d WHERE d.cui = :cui")
        , @NamedQuery(name = "Disease.updateById", query = "UPDATE Disease d SET d.name = :name, d.cui = :cui  WHERE d.diseaseId = :diseaseId")
        , @NamedQuery(name = "Disease.findLastDisease", query = "SELECT d FROM Disease d ORDER BY d.diseaseId DESC")
})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Disease.findByIdNativeMapping",
                query = "SELECT d.disease_id, d.name, d.cui "
                        + "FROM disease d WHERE d.disease_id COLLATE utf8_bin = :diseaseId",
                resultSetMapping="DiseaseMapping"

        ),
        @NamedNativeQuery(
                name = "Disease.findByIdNativeResultClass",
                query = "SELECT d.disease_id, d.name, d.cui "
                        + "FROM disease d WHERE d.disease_id COLLATE utf8_bin = :diseaseId",
                resultClass = Disease.class

        ),
        @NamedNativeQuery(
                name = "Disease.findByNameNative",
                query = "SELECT d.disease_id, d.name, d.cui "
                        + "FROM disease d WHERE UPPER(d.name) COLLATE utf8_bin = UPPER(:name)"
        ),
        @NamedNativeQuery(
                name = "Disease.findByNameNativeUPPER",
                query = "SELECT d.disease_id, d.name, d.cui "
                        + "FROM disease d WHERE UPPER(d.name) COLLATE utf8_bin = UPPER(:name)"
        ),
        @NamedNativeQuery(
                name = "Disease.findByNameNativeUnrestricted",
                query = "SELECT d.disease_id, d.name, d.cui "
                        + "FROM disease d WHERE d.name = :name"
        ),
        @NamedNativeQuery(
                name = "Disease.findLastIdNative",
                query = "SELECT d.disease_id, SUBSTRING( d.disease_id , 4) 'int_disease_id' " +
                        "FROM disease d " +
                        "ORDER BY CAST( SUBSTRING( d.disease_id , 4) AS UNSIGNED) DESC "
        ),


        @NamedNativeQuery(
                name = "Disease.insertNative",
                query = "INSERT INTO disease (disease_id, name, cui) "
                        + "VALUES (:diseaseId, :name, :cui)"
        ),
        @NamedNativeQuery(
                name = "HasDisease.insertNative",
                query = "INSERT IGNORE INTO has_disease (document_id, date, disease_id) "
                        + "VALUES (:documentId, :date, :diseaseId)"
        ),


        @NamedNativeQuery(
                name = "Disease.findAllBySourceAndVersionNative",
                query = "SELECT d.disease_id, d.name, d.cui " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "WHERE s.name COLLATE utf8_bin = :sourceName " +
                        "AND doc.date = :version "
        ),
        @NamedNativeQuery(
                name = "Disease.findByIdAndSourceAndVersionNative",
                query = "SELECT d.disease_id, d.name, d.cui " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "WHERE s.name COLLATE utf8_bin = :sourceName " +
                        "AND doc.date = :version " +
                        "AND d.disease_id = :diseaseId "
        ),
        @NamedNativeQuery(
                name = "Disease.findByCuiAndSourceAndVersionNative",
                query = "SELECT d.disease_id, d.name, d.cui " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "WHERE s.name COLLATE utf8_bin = :sourceName " +
                        "AND doc.date = :version " +
                        "AND d.cui = :cui "
        ),
        @NamedNativeQuery(
                name = "Disease.findByNameAndSourceAndVersionNative",
                query = "SELECT d.disease_id, d.name, d.cui " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "WHERE s.name COLLATE utf8_bin = :sourceName " +
                        "AND doc.date = :version " +
                        "AND d.name LIKE :diseaseName "
        ),
        @NamedNativeQuery(
                name = "Disease.updateCuiByIdAndSourceAndVersionNative",
                query = " UPDATE disease d " +
                        " INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        " INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        " INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        " INNER JOIN source s ON s.source_id = hs.source_id " +
                        " SET d.cui = :cui " +
                        " WHERE s.name COLLATE utf8_bin = :sourceName " +
                        " AND doc.date = :version " +
                        " AND d.disease_id = :diseaseId "
        ),
        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionAndCode",
                query = " SELECT DISTINCT d.disease_id, d.name, hc.code , r.name 'resource'-- , doc.date, doc.document_id, getDocumentUrl(sce.name, doc.date, d.disease_id) 'url' \n" +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "-- source\n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- codes\n" +
                        "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date " +
                        "INNER JOIN code c ON c.code = hc.code AND c.resource_id = hc.resource_id " +
                        "INNER JOIN resource r ON r.resource_id = c.resource_id " +
                        "WHERE -- sce.name = :source " +
                        "-- AND doc.date = :version\n " +
                        "c.code = :code " +
                        "AND r.name = :resource "
        ),
        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionAndCodeAndDiseaseName",
                query = " SELECT DISTINCT d.disease_id, d.name, hc.code , r.name 'resource'-- , doc.date, doc.document_id, getDocumentUrl(sce.name, doc.date, d.disease_id) 'url' \n" +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "-- source\n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- codes\n" +
                        "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date " +
                        "INNER JOIN code c ON c.code = hc.code AND c.resource_id = hc.resource_id " +
                        "INNER JOIN resource r ON r.resource_id = c.resource_id " +
                        "WHERE -- sce.name = :source " +
                        "-- AND doc.date = :version\n " +
                        "c.code = :code " +
                        "AND r.name = :resource " +
                        "AND d.name = :diseaseName"
        ),
        @NamedNativeQuery(
                name = "Disease.findAllWithTheirDISNETTermsBySourceAndSnapshotAndValidated",
                query = " SELECT DISTINCT doc.date snapshot, d.disease_id, d.name 'disease_name', hsym.cui 'cui', sym.name 'term_name', getSemanticTypesBySymptom(hsym.cui) 'semantic_types', IF(hsym.validated, 'true', 'false') 'validated' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- symptoms\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :sourceName " +
                        "AND hs.date = :snapshot " +
                        "-- AND hsym.validated = :validated \n " +
                        "GROUP BY doc.date, d.disease_id, d.name, hsym.cui, sym.name "
        )
})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "DiseaseMapping",
                entities = @EntityResult(
                        entityClass = Disease.class,
                        fields = {
                                @FieldResult(name = "diseaseId", column = "disease_id"),
                                @FieldResult(name = "name", column = "name"),
                                @FieldResult(name = "cui", column = "cui"),
                                @FieldResult(name = "relevant", column = "relevant")
                        }
                )
        )
})

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="diseaseId")
public class Disease {
    private String diseaseId;
    private String name;
    private String cui;
    private Byte relevant;
    private List<HasDisease> hasDiseasesByDiseaseId;
    private List<DiseaseSynonym> diseaseSynonymsByDiseaseId;

    @Id
    @Column(name = "disease_id", nullable = false, length = 150)
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 150)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "cui", nullable = true, length = 8)
    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    @Basic
    @Column(name = "relevant", nullable = true)
    public Byte getRelevant() {
        return relevant;
    }

    public void setRelevant(Byte relevant) {
        this.relevant = relevant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return Objects.equals(diseaseId, disease.diseaseId) &&
                Objects.equals(name, disease.name) &&
                Objects.equals(cui, disease.cui);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diseaseId, name, cui);
    }

    @OneToMany(mappedBy = "diseaseByDiseaseId")
    public List<HasDisease> getHasDiseasesByDiseaseId() {
        return hasDiseasesByDiseaseId;
    }

    public void setHasDiseasesByDiseaseId(List<HasDisease> hasDiseasesByDiseaseId) {
        this.hasDiseasesByDiseaseId = hasDiseasesByDiseaseId;
    }

    @OneToMany(mappedBy = "diseaseByDiseaseId")
    public List<DiseaseSynonym> getDiseaseSynonymsByDiseaseId() {
        return diseaseSynonymsByDiseaseId;
    }

    public void setDiseaseSynonymsByDiseaseId(List<DiseaseSynonym> diseaseSynonymsByDiseaseId) {
        this.diseaseSynonymsByDiseaseId = diseaseSynonymsByDiseaseId;
    }


    @Override
    public String toString() {
        return "Disease{" +
                "diseaseId='" + diseaseId + '\'' +
                ", name='" + name + '\'' +
                ", cui='" + cui + '\'' +
                '}';
    }
}
