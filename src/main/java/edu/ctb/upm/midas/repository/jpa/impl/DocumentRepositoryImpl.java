package edu.ctb.upm.midas.repository.jpa.impl;
import edu.ctb.upm.midas.model.jpa.Document;
import edu.ctb.upm.midas.model.jpa.DocumentPK;
import edu.ctb.upm.midas.repository.jpa.AbstractDao;
import edu.ctb.upm.midas.repository.jpa.DocumentRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DocumentRepositoryImpl
 * @see
 */
@Repository("DocumentRepositoryDao")
public class DocumentRepositoryImpl extends AbstractDao<DocumentPK, Document>
                                    implements DocumentRepository {


    public Document findById(DocumentPK documentPK) {
        Document document = getByKey(documentPK);
        return document;
    }

    @SuppressWarnings("unchecked")
    public Document findByIdQuery(DocumentPK documentPK) {
        Document document = null;
        List<Document> documentList = (List<Document>) getEntityManager()
                .createNamedQuery("Document.findById")
                .setParameter("documentId", documentPK.getDocumentId())
                .setParameter("date", documentPK.getDate())
                .getResultList();
        if (CollectionUtils.isNotEmpty(documentList))
            document = documentList.get(0);
        return document;
    }

    @SuppressWarnings("unchecked")
    public Document findByIdNative(DocumentPK documentPK) {
        Document document = null;
        List<Document> documentList = (List<Document>) getEntityManager()
                .createNamedQuery("Document.findByIdNative")
                .setParameter("documentId", documentPK.getDocumentId())
                .setParameter("date", documentPK.getDate())
                .getResultList();
        if (CollectionUtils.isNotEmpty(documentList))
            document = documentList.get(0);
        return document;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Document findByIdNativeResultClass(DocumentPK documentPK) {
        Document document = null;
        List<Document> documentList = (List<Document>) getEntityManager()
                .createNamedQuery("Document.findByIdNativeResultClass")
                .setParameter("documentId", documentPK.getDocumentId())
                .setParameter("date", documentPK.getDate())
                .getResultList();
        if (CollectionUtils.isNotEmpty(documentList))
            document = documentList.get(0);
        return document;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Date findLastVersionNative(String source) {
        Date version = null;
        List<Date> versionList = (List<Date>) getEntityManager()
                .createNamedQuery("Document.findLastVersionNative")
                .setParameter("source", source)
                .setMaxResults(1)
                .getResultList();
        if (CollectionUtils.isNotEmpty(versionList))
            version = versionList.get(0);
        return version;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Document> findAllQuery() {
        return (List<Document>) getEntityManager()
                .createNamedQuery("Document.findAll")
//                .setMaxResults(10)
                .setMaxResults(0)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Document> findAllBySourceIdAndSnapshot(Date snapshot, String sourceId) {
        return (List<Document>) getEntityManager()
                .createNamedQuery("Document.findBySourceAndSnapshot")
                .setParameter("sourceId", sourceId)
                .setParameter("snapshot", snapshot)
//                .setMaxResults(10)
                .setMaxResults(0)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Object[]> findAllArticlesAndSnapshot() {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Document.findAllArticlesAndSnapshot")
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;

        return diseases;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Object[]> findAllDistinctArticlesAndSnapshot() {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Document.findAllDistinctArticlesAndSnapshot")
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;

        return diseases;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Object[]> findAllSnapshotsOfAArticle(String diseaseId) {
        List<Object[]> revisions = null;
        List<Object[]> revisionList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Document.findAllSnapshotsOfAArticle")
                .setParameter("diseaseId", diseaseId)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(revisionList))
            revisions = revisionList;

        return revisions;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Object[]> getDiseaseDKEInfoToAnalysis(String diseaseId) {
        List<Object[]> snapshots = null;
        List<Object[]> snapshotList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Document.getDiseaseDKEInfoToAnalysis")
                .setParameter("diseaseId", diseaseId)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(snapshotList))
            snapshots = snapshotList;

        return snapshots;
    }

    public void persist(Document document) {
        super.persist(document);
    }

    @Override
    public int insertNative(String documentId, Date date) {
        return getEntityManager()
                .createNamedQuery("Document.insertNative")
                .setParameter("documentId", documentId)
                .setParameter("date", date)
                .executeUpdate();
    }

    @Override
    public int insertNativeUrl(String documentId, Date date, String urlId) {
        return getEntityManager()
                .createNamedQuery("DocumentUrl.insertNative")
                .setParameter("documentId", documentId)
                .setParameter("date", date)
                .setParameter("urlId", urlId)
                .executeUpdate();
    }

    @Override
    public int insertNativeHasSource(String documentId, Date date, String sourceId) {
        return getEntityManager()
                .createNamedQuery("HasSource.insertNative")
                .setParameter("documentId", documentId)
                .setParameter("date", date)
                .setParameter("sourceId", sourceId)
                .executeUpdate();
    }

    public boolean deleteById(DocumentPK documentPK) {
        Document document = findById( documentPK );
        if(document ==null)
            return false;
        super.delete(document);
        return true;
    }

    public void delete(Document document) {
        super.delete(document);
    }

    public Document update(Document document) {
        return super.update(document);
    }

    @Override
    public int updateByIdQuery(Document document) {
        return 0;
    }
}
