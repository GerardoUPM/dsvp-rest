package edu.ctb.upm.midas.repository.jpa.impl;

import edu.ctb.upm.midas.model.jpa.Source;
import edu.ctb.upm.midas.repository.jpa.AbstractDao;
import edu.ctb.upm.midas.repository.jpa.SourceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 28/04/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project ExtractionInformationDiseasesWikipedia
 * @className SourceRepositoryImpl
 * @see
 */
@Repository("SourceRepositoryDao")
public class SourceRepositoryImpl extends AbstractDao<String, Source> implements SourceRepository {


    public Source findById(String sourceId) {
        Source source = getByKey(sourceId);
        return source;
    }

    @SuppressWarnings("unchecked")
    public Source findByIdQuery(String sourceId) {
        Source source = null;
        List<Source> listSource = (List<Source>) getEntityManager()
                .createNamedQuery("Source.findById")
                .setParameter("sourceId", sourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @SuppressWarnings("unchecked")
    public Source findByNameQuery(String sourceName) {
        Source source = null;
        List<Source> listSource = (List<Source>) getEntityManager()
                .createNamedQuery("Source.findByName")
                .setParameter("name", sourceName)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @Override
    public String findByNameNative(String sourceName) {
        String sourceId = (String) getEntityManager()
                .createNamedQuery("Source.findByNameNative")
                .setParameter("name", sourceName)
                .setMaxResults(1)
                .getSingleResult();
        return sourceId;
    }

    @Override
    public Source findLastSourceQuery() {
        Source source = null;
        source = (Source) getEntityManager()
                .createNamedQuery("Source.findLastSourceNativeResultClass")
                .setMaxResults(1)
                .getResultList();
        return source;
    }

    @Override
    public String findLastSourceIdQuery() {
        String sourceId = (String) getEntityManager()
                .createNamedQuery("Source.findLastIdNative")
                .setMaxResults(1)
                .getSingleResult();
        return sourceId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Source findByIdNative(String sourceId) {
        Source source = null;
        List<Source> listSource = (List<Source>) getEntityManager()
                .createNamedQuery("Source.findByIdNativeMapping")
                .setParameter("sourceId", sourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Source findByIdNativeResultClass(String sourceId) {
        Source source = null;
        List<Source> listSource = (List<Source>) getEntityManager()
                .createNamedQuery("Source.findByIdNativeResultClass")
                .setParameter("sourceId", sourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Date> findAllSnapshotBySourceNative(String source) {
        List<Date> snapshots = null;
        List<Date> snapshotList = (List<Date>) getEntityManager()
                .createNamedQuery("Source.findAllSnapshotBySourceNative")
                .setParameter("name", source)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(snapshotList)) {
            snapshots = snapshotList;
        }
        return snapshots;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Source> findAllQuery() {
        /*List_<Source> sources = getEntityManager()
                .createNamedQuery("Source.findAll")
                .setMaxResults(10)
                .getResultList();
        return sources;*/
        return (List<Source>) getEntityManager()
                .createNamedQuery("Source.findAll")
                .setMaxResults(0)
                .getResultList();
    }

    @Override
    public void persist(Source source) {
        super.persist(source);
    }

    @Override
    public int insertNative(String sourceId, String name) {
        return getEntityManager()
                .createNamedQuery("Source.insertNative")
                .setParameter("sourceId", sourceId)
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    public int insertNativeUrl(String sourceId, String urlId) {
        return getEntityManager()
                .createNamedQuery("SourceUrl.insertNative")
                .setParameter("sourceId", sourceId)
                .setParameter("urlId", urlId)
                .executeUpdate();
    }

    @Override
    public boolean deleteById(String sourceId) {
        Source source = findById( sourceId );
        if(source ==null)
            return false;
        super.delete(source);
        return true;
    }

    @Override
    public void delete(Source source) {
        super.delete(source);
    }

    @Override
    public Source update(Source source) {
        return super.update(source);
    }

    @Override
    public int updateByIdQuery(Source source) {
        /*int rows = getEntityManager()
                .createNamedQuery("Source.updateById")
                .setParameter("sourceId", source.getSourceEntityId())
                .setParameter("lastUpdate", source.getLastUpdate())
                .executeUpdate();
        return rows;*/
        return getEntityManager()
                .createNamedQuery("Source.updateById")
                .setParameter("sourceId", source.getSourceId())
                .executeUpdate();
    }
}
