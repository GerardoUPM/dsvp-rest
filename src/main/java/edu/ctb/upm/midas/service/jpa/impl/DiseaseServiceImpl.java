package edu.ctb.upm.midas.service.jpa.impl;
import edu.ctb.upm.midas.model.jpa.Disease;
import edu.ctb.upm.midas.repository.jpa.DiseaseRepository;
import edu.ctb.upm.midas.service.jpa.DiseaseService;
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
    public Disease findByNameNative(String diseaseName) {
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

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findByNameNativeUnrestricted(String diseaseName) {
        Disease disease = null;
        Object[] oQuery = daoDisease.findByNameNativeUnrestricted(diseaseName);
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
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Object[]> findAllBySourceAndVersionNative(String sourceName, Date version) {
        return daoDisease.findAllBySourceAndVersionNative(sourceName, version);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Disease> findAllBySourceAndVersionNativeDiseases(String sourceName, Date version) {
        List<Disease> diseaseList = new ArrayList<>();
        List<Object[]> objectDisease = daoDisease.findAllBySourceAndVersionNative(sourceName, version);
        if (objectDisease!=null){
            diseaseList = new ArrayList<>();
            for (Object[] o: objectDisease) {
                Disease disease = new Disease();
                disease.setDiseaseId((String) o[0]);
                disease.setName((String) o[1]);
                diseaseList.add(disease);
            }
        }
        return diseaseList;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Object[] findByIdAndSourceAndVersionNative(String diseaseId, String sourceName, Date version) {
        return daoDisease.findByIdAndSourceAndVersionNative(diseaseId, sourceName, version);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Object[] findByCuiAndSourceAndVersionNative(String cui, String sourceName, Date version) {
        return daoDisease.findByCuiAndSourceAndVersionNative(cui, sourceName, version);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Disease> findBySourceAndVersionAndCode(String sourceName, Date version, String code, String resourceName) {
        List<Disease> diseases = null;
        List<Object[]> objectDisease = daoDisease.findBySourceAndVersionAndCode(sourceName, version, code, resourceName);
        if (objectDisease!=null){
            diseases = new ArrayList<>();
            for (Object[] o: objectDisease) {
                Disease disease = new Disease();
                disease.setDiseaseId((String) o[0]);
                disease.setName((String) o[1]);
                diseases.add(disease);
            }
        }
        return diseases;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findOneBySourceAndVersionAndCode(String sourceName, Date version, String code, String resourceName) {
        Disease disease = null;
        List<Object[]> objectDisease = daoDisease.findBySourceAndVersionAndCode(sourceName, version, code, resourceName);
        if (objectDisease!=null){
            for (Object[] o: objectDisease) {
                disease = new Disease();
                disease.setDiseaseId((String) o[0]);
                disease.setName((String) o[1]);
                break;
            }
        }
        return disease;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findBySourceAndVersionAndCodeAndDiseaseName(String sourceName, Date version, String code, String resourceName, String diseaseName) {
        Disease disease = null;
        Object[] objectDisease = daoDisease.findBySourceAndVersionAndCodeAndDiseaseName(sourceName, version, code, resourceName, diseaseName);
        if (objectDisease!=null){
            disease = new Disease();
            disease.setDiseaseId((String) objectDisease[0]);
            disease.setName((String) objectDisease[1]);
        }
        return disease;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Object[]> findAllWithTheirDISNETTermsBySourceAndSnapshotAndValidated(String sourceName, Date snapshot, boolean validated) {
        List<Object[]> objectDisease = daoDisease.findAllWithTheirDISNETTermsBySourceAndSnapshotAndValidated(sourceName, snapshot, validated);
        return objectDisease;
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
