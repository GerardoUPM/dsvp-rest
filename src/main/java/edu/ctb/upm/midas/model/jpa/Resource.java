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
 * @className Resource
 * @see
 */
@Entity
@Table(name = "resource", catalog = "edsssdb", schema = "")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Resource.findAll", query = "SELECT r FROM Resource r")
        , @NamedQuery(name = "Resource.findByResourceId", query = "SELECT r FROM Resource r WHERE r.resourceId = :resourceId")
        , @NamedQuery(name = "Resource.findById", query = "SELECT r FROM Resource r WHERE r.resourceId = :resourceId")
        , @NamedQuery(name = "Resource.findByName", query = "SELECT r FROM Resource r WHERE r.name = :name")
        , @NamedQuery(name = "Resource.updateById", query = "UPDATE Resource r SET r.name = :name WHERE r.resourceId = :resourceId")
})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Resource.findByIdNative",
                query = "SELECT r.resource_id, r.name "
                        + "FROM resource r WHERE r.resource_id = :resourceId",
                resultSetMapping="ResourceMapping"

        ),
        @NamedNativeQuery(
                name = "Resource.findByIdNativeResultClass",
                query = "SELECT r.resource_id, r.name "
                        + "FROM resource r WHERE r.resource_id = :resourceId",
                resultClass = Resource.class

        ),
        @NamedNativeQuery(
                name = "Resource.findByNameNative",
                query = "SELECT r.resource_id, r.name "
                        + "FROM resource r WHERE r.name COLLATE utf8_bin = :name",
                resultClass = Resource.class
        ),
        @NamedNativeQuery(
                name = "Resource.findIdByNameNative",
                query = "SELECT r.resource_id "
                        + "FROM resource r WHERE r.name COLLATE utf8_bin = :name"
        ),


        @NamedNativeQuery(
                name = "Resource.insertNative",
                query = "INSERT IGNORE INTO resource (name) "
                        + "VALUES (:name)"
        )
})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "ResourceMapping",
                entities = @EntityResult(
                        entityClass = Resource.class,
                        fields = {
                                @FieldResult(name = "resourceId", column = "resource_id"),
                                @FieldResult(name = "name", column = "name"),
                        }
                )
        )
})

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="resourceId")
public class Resource {
    private Integer resourceId;
    private String name;
    private List<Code> codesByResourceId;
    private List<Term> termsByResourceId;

    @Id
    @Column(name = "resource_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(resourceId, resource.resourceId) &&
                Objects.equals(name, resource.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId, name);
    }

    @OneToMany(mappedBy = "resourceByResourceId")
    public List<Code> getCodesByResourceId() {
        return codesByResourceId;
    }

    public void setCodesByResourceId(List<Code> codesByResourceId) {
        this.codesByResourceId = codesByResourceId;
    }

    @OneToMany(mappedBy = "resourceByResourceId")
    public List<Term> getTermsByResourceId() {
        return termsByResourceId;
    }

    public void setTermsByResourceId(List<Term> termsByResourceId) {
        this.termsByResourceId = termsByResourceId;
    }

}
