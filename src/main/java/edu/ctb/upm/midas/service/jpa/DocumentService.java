package edu.ctb.upm.midas.service.jpa;

import edu.ctb.upm.midas.model.jpa.Document;
import edu.ctb.upm.midas.model.jpa.DocumentPK;
import edu.ctb.upm.midas.model.wikipediaApi.Disease;
import edu.ctb.upm.midas.model.wikipediaApi.Snapshot;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DocumentService
 * @see
 */
public interface DocumentService {

    Document findById(DocumentPK documentPk);

    Date findLastVersionNative(String source);

    List<Document> findAll();

    List<Document> findAllBySourceIdAndSnapshot(Date snapshot, String sourceId);

    List<Disease> findAllArticlesAndSnapshot();

    List<Disease> findAllDistinctArticlesAndSnapshot();

    List<Snapshot> findAllSnapshotsOfAArticle(String diseaseId);

    List<Object[]> getDiseaseDKEInfoToAnalysis(String diseaseId);

    void save(Document document);

    int insertNative(String documentId, Date date);

    int insertNativeUrl(String documentId, Date date, String urlId);

    int insertNativeHasSource(String documentId, Date date, String sourceId);

    boolean updateFindFull(Document document, DocumentPK documentPk);

    boolean updateFindPartial(Document document, DocumentPK documentPk);

    boolean deleteById(DocumentPK documentPk);
    
}
