package edu.ctb.upm.midas.repository.jpa.impl;
import edu.ctb.upm.midas.model.jpa.HasSymptom;
import edu.ctb.upm.midas.model.jpa.HasSymptomPK;
import edu.ctb.upm.midas.repository.jpa.AbstractDao;
import edu.ctb.upm.midas.repository.jpa.HasSymptomRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className HasSymptomServiceImpl
 * @see
 */
@Repository("HasSymptomRepositoryDao")
public class HasSymptomRepositoryImpl extends AbstractDao<HasSymptomPK, HasSymptom>
                                        implements HasSymptomRepository {

    private static final Logger logger = LoggerFactory.getLogger(HasSymptomRepositoryImpl.class);


    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    public HasSymptom findById(HasSymptomPK hasSymptomPK) {
        HasSymptom hasSymptom = getByKey(hasSymptomPK);
        return hasSymptom;
    }

    @SuppressWarnings("unchecked")
    public HasSymptom findByIdQuery(HasSymptomPK hasSymptomPK) {
        HasSymptom hasSymptom = null;
        List<HasSymptom> hasSymptomList = (List<HasSymptom>) getEntityManager()
                .createNamedQuery("HasSymptom.findById")
                .setParameter("textId", hasSymptomPK.getTextId())
                .setParameter("cui", hasSymptomPK.getCui())
                .getResultList();
        if (CollectionUtils.isNotEmpty(hasSymptomList))
            hasSymptom = hasSymptomList.get(0);
        return hasSymptom;
    }

    @SuppressWarnings("unchecked")
    public HasSymptom findByIdNative(HasSymptomPK hasSymptomPK) {
        HasSymptom hasSymptom = null;
        List<HasSymptom> hasSymptomList = (List<HasSymptom>) getEntityManager()
                .createNamedQuery("HasSymptom.findByIdNative")
                .setParameter("textId", hasSymptomPK.getTextId())
                .setParameter("cui", hasSymptomPK.getCui())
                .getResultList();
        if (CollectionUtils.isNotEmpty(hasSymptomList))
            hasSymptom = hasSymptomList.get(0);
        return hasSymptom;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HasSymptom findByIdNativeResultClass(HasSymptomPK hasSymptomPK) {
        HasSymptom hasSymptom = null;
        List<HasSymptom> hasSymptomList = (List<HasSymptom>) getEntityManager()
                .createNamedQuery("HasSymptom.findByIdNativeResultClass")
                .setParameter("textId", hasSymptomPK.getTextId())
                .setParameter("cui", hasSymptomPK.getCui())
                .getResultList();
        if (CollectionUtils.isNotEmpty(hasSymptomList))
            hasSymptom = hasSymptomList.get(0);
        return hasSymptom;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HasSymptom> findAllQuery() {
        return (List<HasSymptom>) getEntityManager()
                .createNamedQuery("HasSymptom.findAll")
                .setMaxResults(0)
                .getResultList();
    }

    @Override
    public void persist(HasSymptom hasSymptom) {
        super.persist(hasSymptom);
    }

    @Override
    public int insertNative(String textId, String cui, boolean validated, String matchedWords, String positionalInfo) {
        return getEntityManager()
                .createNamedQuery("HasSymptom.insertNative")
                .setParameter("textId", textId)
                .setParameter("cui", cui)
                .setParameter("validated", validated)//, String matchedWords, String positionalInfo
                .setParameter("matchedWords", matchedWords)
                .setParameter("positionalInfo", positionalInfo)
                .executeUpdate()
                ;
    }

//    @Override
//    public int insertMassiveNative(String insertMassiveSQL) throws SQLException {
//        PreparedStatement ps = getConnection().prepareStatement().;
//        ps.setString(1, );
//
//    }

    @Override
    public int insertInBatch(List<HasSymptom> entityList){
        int count = 0;
        for (HasSymptom hasSymptom: entityList) {
            super.persist(hasSymptom);
            count++;
            if (count % batchSize == 0) {
                getEntityManager().flush();
                getEntityManager().clear();
            }
        }
        return count;
    }

    @Override
    public boolean deleteById(HasSymptomPK hasSymptomPK) {
        return false;
    }

    @Override
    public void delete(HasSymptom hasSymptom) {
        super.delete(hasSymptom);
    }

    @Override
    public HasSymptom update(HasSymptom hasSymptom) {
        return super.update(hasSymptom);
    }

    @Override
    public int updateByIdQuery(HasSymptom hasSymptom) {
        return 0;
    }

    @Override
    public int updateValidatedNative(String version, String sourceId, String cui, boolean validated) {
        return getEntityManager()
                .createNamedQuery("HasSymptom.updateValidatedNative")
                .setParameter("version", "%" + version + "%")
                .setParameter("sourceId", "%" + sourceId + "%")
                .setParameter("cui", cui)
                .setParameter("validated", validated)
                .executeUpdate();
    }

    @Override
    public int updateMatchedWordsAndPositionalInfoNative(String textId, String cui, String matchedWords, String positionalInfo) {
        return getEntityManager()
                .createNamedQuery("HasSymptom.updateMatchedWordsAndPositionalInfoNative")
                .setParameter("textId", textId)
                .setParameter("cui", cui)
                .setParameter("matchedWords", matchedWords)
                .setParameter("positionalInfo", positionalInfo)
                .executeUpdate();
    }
}
