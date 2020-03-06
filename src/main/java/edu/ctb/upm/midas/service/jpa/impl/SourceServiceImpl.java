package edu.ctb.upm.midas.service.jpa.impl;

import edu.ctb.upm.midas.model.jpa.Source;
import edu.ctb.upm.midas.repository.jpa.SourceRepository;
import edu.ctb.upm.midas.service.jpa.SourceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 03/05/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project ExtractionInformationDiseasesWikipedia
 * @className SourceServiceImpl
 * @see
 */
@Service("sourceService")
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceRepository daoSource;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Source findById(String sourceId) {
        Source source = daoSource.findById((String) sourceId);
        //if(source!=null)
            //Hibernate.initialize(source.getDiseasesBySidsource());
        return source;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Source findByName(String sourceName) {
        Source source = daoSource.findByNameQuery(sourceName);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return source;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public String findByNameNative(String sourceName) {
        return daoSource.findByNameNative( sourceName );
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Source findLastSourceQuery() {
        return daoSource.findLastSourceQuery();
    }

    @Override
    public String findLastSourceIdQuery() {
        return daoSource.findLastSourceIdQuery();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Date> findAllSnapshotBySourceNative(String source) {
        return daoSource.findAllSnapshotBySourceNative( source );
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Source> findAll() {
        List<Source> listSourceEntities = daoSource.findAllQuery();
        return listSourceEntities;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Source source) {
        daoSource.persist(source);
    }

    @Override
    public int insertNative(String sourceId, String name) {
        return daoSource.insertNative( sourceId, name );
    }

    @Override
    public int insertNativeUrl(String sourceId, String urlId) {
        return daoSource.insertNativeUrl( sourceId, urlId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Source source) {
        Source sour = daoSource.findById(source.getSourceId());
        if(sour!=null){
            sour.setSourceId(source.getSourceId());
            sour.setName(source.getName());
            //sour.getDiseasesBySidsource().clear();
            //sour.getDiseasesBySidsource().addAll(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource())?source.getDiseasesBySidsource():new ArrayList<Disease>());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Source source) {
        Source sour = daoSource.findById(source.getSourceId());
        if(sour!=null){
            if(StringUtils.isNotBlank(source.getSourceId()))
                sour.setSourceId(source.getSourceId());
            if(StringUtils.isNotBlank(source.getName()))
                sour.setName(source.getName());
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(String sourceId) {
        Source source = daoSource.findById(sourceId);
        if(source !=null)
            daoSource.delete(source);
        else
            return false;
        return true;
    }
}
