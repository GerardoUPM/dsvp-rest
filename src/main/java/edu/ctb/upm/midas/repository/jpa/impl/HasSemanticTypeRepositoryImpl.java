package edu.ctb.upm.midas.repository.jpa.impl;
import edu.ctb.upm.midas.model.jpa.HasSemanticType;
import edu.ctb.upm.midas.model.jpa.HasSemanticTypePK;
import edu.ctb.upm.midas.repository.jpa.AbstractDao;
import edu.ctb.upm.midas.repository.jpa.HasSemanticTypeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Created by gerardo on 29/06/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className HasSemanticTypeRepositoryImpl
 * @see
 */
@Repository("HasSemanticTypeRepositoryDao")
public class HasSemanticTypeRepositoryImpl extends AbstractDao<HasSemanticTypePK, HasSemanticType> implements HasSemanticTypeRepository {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @SuppressWarnings("unchecked")
    @Override
    public HasSemanticType findById(HasSemanticTypePK hasSemanticTypePK) {
        HasSemanticType hasSemanticType = getByKey(hasSemanticTypePK);
        return hasSemanticType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HasSemanticType findByIdQuery(HasSemanticTypePK hasSemanticTypePK) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HasSemanticType findByIdNative(HasSemanticTypePK hasSemanticTypePK) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HasSemanticType> findAllQuery() {
        return (List<HasSemanticType>) getEntityManager()
                .createNamedQuery("HasSemanticType.findAll")
                .setMaxResults(0)
                .getResultList();
    }

    @Override
    public void persist(HasSemanticType hasSemanticType) {
        super.persist(hasSemanticType);
    }

    @Override
    public int insertInBatch(List<HasSemanticType> entityList) {
        int count = 0;
        for (HasSemanticType hasSemanticType: entityList) {
            super.persist(hasSemanticType);
            count++;
            if (count % batchSize == 0) {
                getEntityManager().flush();
                getEntityManager().clear();
            }
        }
        return count;
    }

    @Override
    public boolean deleteById(HasSemanticTypePK hasSemanticTypePK) {
        return false;
    }

    @Override
    public void delete(HasSemanticType hasSemanticType) {
        super.delete(hasSemanticType);
    }

    @Override
    public HasSemanticType update(HasSemanticType hasSemanticType) {
        return super.update(hasSemanticType);
    }

    @Override
    public int updateByIdQuery(HasSemanticType hasSemanticType) {
        return 0;
    }
}
