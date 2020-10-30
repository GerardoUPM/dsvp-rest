package edu.ctb.upm.midas.service.jpa.impl;
import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.model.jpa.Document;
import edu.ctb.upm.midas.model.jpa.DocumentPK;
import edu.ctb.upm.midas.model.wikipediaApi.Disease;
import edu.ctb.upm.midas.model.wikipediaApi.Snapshot;
import edu.ctb.upm.midas.repository.jpa.DocumentRepository;
import edu.ctb.upm.midas.service.jpa.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DocumentServiceImpl
 * @see
 */
@Service("documentService")
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository daoDocument;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Document findById(DocumentPK documentPk) {
        Document document = daoDocument.findById( documentPk );
        /*if(document!=null)
            Hibernate.initialize(document.getCodeList());*/
        return document;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Date findLastVersionNative(String source) {
        return daoDocument.findLastVersionNative(source);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Document> findAll() {
        List<Document> listDocumentEntities = daoDocument.findAllQuery();
        return listDocumentEntities;
    }

    @Override
    public List<Document> findAllBySourceIdAndSnapshot(Date snapshot, String sourceId) {
        return daoDocument.findAllBySourceIdAndSnapshot(snapshot, sourceId);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    @Override
    public List<Disease> findAllArticlesAndSnapshot() {
        List<Object[]> objects = daoDocument.findAllArticlesAndSnapshot();
        return createDiseaseList(objects, false);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    @Override
    public List<Disease> findAllDistinctArticlesAndSnapshot() {
        List<Object[]> objects = daoDocument.findAllDistinctArticlesAndSnapshot();
        return createDiseaseList(objects, true);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    @Override
    public List<Snapshot> findAllSnapshotsOfAArticle(String diseaseId) {
        TimeProvider timeProvider = new TimeProvider();
        List<Snapshot> snapshots = new ArrayList<>();
        List<Object[]> objects = daoDocument.findAllSnapshotsOfAArticle(diseaseId);
        if (objects!=null){
            snapshots = new ArrayList<>();
            for (Object[] obj: objects) {
                Snapshot snapshot = new Snapshot(
                        (Integer) obj[0]
                        , timeProvider.sqlDateFormatyyyyMMdd( ((java.sql.Date) obj[1]) )
                        , (obj[2]==null)?"":timeProvider.sqlDateFormatyyyyMMdd( (java.sql.Date) obj[2] )
                );
                snapshots.add(snapshot);
            }
        }
        return snapshots;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    @Override
    public List<Object[]> getDiseaseDKEInfoToAnalysis(String diseaseId) {
        return daoDocument.getDiseaseDKEInfoToAnalysis(diseaseId);
    }

    public List<Disease> createDiseaseList(List<Object[]> objects, boolean basicInfo){
        List<Disease> diseases = new ArrayList<>();
        if (objects!=null){
            diseases = new ArrayList<>();
            for (Object[] obj: objects) {
                Disease disease = new Disease();
                if (basicInfo){
                    disease.setId((String) obj[0]);
                    disease.setName((String) obj[1]);
                }else {
                    disease.setId((String) obj[0]);
                    disease.setName((String) obj[1]);
//                    disease.setSnapshotId((Integer) obj[2]);
//                    disease.setCurrentSnapshot((Date) obj[3]);
//                    disease.setPreviousSnapshot((Date) obj[4]);
                }
                diseases.add(disease);
            }
        }
        return diseases;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Document document) {
        daoDocument.persist(document);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNative(String documentId, Date date) {
        return daoDocument.insertNative( documentId, date );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNativeUrl(String documentId, Date date, String urlId) {
        return daoDocument.insertNativeUrl( documentId, date, urlId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNativeHasSource(String documentId, Date date, String sourceId) {
        return daoDocument.insertNativeHasSource( documentId, date, sourceId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Document document, DocumentPK documentPk) {
        Document doc = daoDocument.findById( documentPk );
        if(doc!=null){
            //==>doc.setDocumentPK(document.getDocumentPK());
            //sour.getDiseasesBySidsource().clear();
            //sour.getDiseasesBySidsource().addAll(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource())?source.getDiseasesBySidsource():new ArrayList<Disease>());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Document document, DocumentPK documentPk) {
        Document doc = daoDocument.findById( documentPk );
        if(doc!=null){
            //==>if(StringUtils.isNotBlank( doc.getDocumentPK().toString() ) )
            //==>    doc.setDocumentPK(document.getDocumentPK());
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(DocumentPK documentPk) {
        Document document = daoDocument.findById(documentPk);
        if(document !=null)
            daoDocument.delete(document);
        else
            return false;
        return true;
    }
}
