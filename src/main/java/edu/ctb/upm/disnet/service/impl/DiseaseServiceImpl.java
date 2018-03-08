package edu.ctb.upm.disnet.service.impl;
import edu.ctb.upm.disnet.model.jpa.Disease;
import edu.ctb.upm.disnet.repository.DiseaseRepository;
import edu.ctb.upm.disnet.service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
 * @className DiseaseServiceImpl
 * @see
 */
@Service("diseaseService")
public class DiseaseServiceImpl implements DiseaseService {

    @Autowired
    private DiseaseRepository daoDisease;


    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findById(String diseaseId) {
        Disease disease = daoDisease.findById(diseaseId);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return disease;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findByName(String diseaseName) {
        Disease disease = null;
        Object[] oQuery = daoDisease.findByNameNative(diseaseName);
        if (oQuery != null){
            disease = new Disease();
            disease.setDiseaseId( (String) oQuery[0] );
            disease.setName( (String) oQuery[1] );
            disease.setCui( (String) oQuery[2] );
        }
        return disease;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findByCui(String cui) {
        Disease disease = daoDisease.findByCuiQuery(cui);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return disease;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    @Override
    public Disease findLastDiseaseQuery() {
        return daoDisease.findLastDiseaseQuery();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    @Override
    public String findLastIdNative() {
        String diseaseId = null;
        Object[] oQuery = daoDisease.findLastIdNative();
        if (oQuery != null){
            diseaseId = (String) oQuery[0];
        }
        return diseaseId;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Disease> findAll() {
        List<Disease> listDiseaseEntities = daoDisease.findAllQuery();
        return listDiseaseEntities;
    }

    @Override
    public List<Object[]> findAllBySourceAndVersionNative(String sourceName, Date version) {
        return daoDisease.findAllBySourceAndVersionNative(sourceName, version);
    }

    @Override
    public Object[] findByIdAndSourceAndVersionNative(String diseaseId, String sourceName, Date version) {
        return daoDisease.findByIdAndSourceAndVersionNative(diseaseId, sourceName, version);
    }

    @Override
    public Object[] findByCuiAndSourceAndVersionNative(String cui, String sourceName, Date version) {
        return daoDisease.findByCuiAndSourceAndVersionNative(cui, sourceName, version);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Disease disease) {
        daoDisease.persist(disease);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNative(String diseaseId, String name, String cui) {
        return daoDisease.insertNative( diseaseId, name, cui);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNativeHasDisease(String documentId, Date date, String diseaseId) {
        return daoDisease.insertNativeHasDisease( documentId, date, diseaseId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Disease disease) {
        Disease dis = daoDisease.findById(disease.getDiseaseId());
        if(dis!=null){
            dis.setDiseaseId(disease.getDiseaseId());
            dis.setName(disease.getName());
            dis.setCui(disease.getCui());
            //sour.getDiseasesBySidsource().clear();
            //sour.getDiseasesBySidsource().addAll(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource())?source.getDiseasesBySidsource():new ArrayList<Disease>());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Disease disease) {
        Disease dis = null;//daoDisease.findByNameNative(disease.getName());
        if(dis!=null){
/*
            if(StringUtils.isNotBlank(disease.getDocumentId()))
                dis.setDocumentId(disease.getDocumentId());
            if(StringUtils.isNotBlank(disease.getName()))
                dis.setName(disease.getName());
*/

            //dis.setDocumentList( disease.getDocumentList() );
/*
            if(StringUtils.isNotBlank(disease.getCui()))
                dis.setCui(disease.getCui());
*/
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(String diseaseId) {
        Disease disease = daoDisease.findById(diseaseId);
        if(disease !=null)
            daoDisease.delete(disease);
        else
            return false;
        return true;
    }

    @Override
    public int updateCuiByIdAndSourceAndVersionNative(String diseaseId, String cui, String sourceName, Date version) {
        return daoDisease.updateCuiByIdAndSourceAndVersionNative(diseaseId, cui, sourceName, version);
    }
}
