package edu.ctb.upm.midas.repository.jpa;

import edu.ctb.upm.midas.model.jpa.Document;
import edu.ctb.upm.midas.model.jpa.DocumentPK;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DocumentRepository
 * @see
 */
public interface DocumentRepository {

    Document findById(DocumentPK documentPK);

    Document findByIdQuery(DocumentPK documentPK);
    
    Document findByIdNative(DocumentPK documentPK);

    Document findByIdNativeResultClass(DocumentPK documentPK);

    Date findLastVersionNative(String source);

    List<Document> findAllQuery();

    List<Document> findAllBySourceIdAndSnapshot(Date snapshot, String sourceId);

    List<Object[]> findAllArticlesAndSnapshot();

    List<Object[]> findAllDistinctArticlesAndSnapshot();

    List<Object[]> findAllSnapshotsOfAArticle(String diseaseId);

    List<Object[]> getDiseaseDKEInfoToAnalysis(String diseaseId);

    void persist(Document document);

    int insertNative(String documentId, Date date);

    int insertNativeUrl(String documentId, Date date, String urlId);

    int insertNativeHasSource(String documentId, Date date, String sourceId);

    boolean deleteById(DocumentPK documentPK);

    void delete(Document document);

    Document update(Document document);

    int updateByIdQuery(Document document);
    
}
