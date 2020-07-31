package edu.ctb.upm.midas.service.jpa.impl;
import edu.ctb.upm.midas.model.jpa.Symptom;
import edu.ctb.upm.midas.repository.jpa.SymptomRepository;
import edu.ctb.upm.midas.service.jpa.SymptomService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SymptomServiceImpl
 * @see
 */
@Service
public class SymptomServiceImpl implements SymptomService {

    @Autowired
    private SymptomRepository daoSymptom;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Symptom findById(String cui) {
        Symptom symptom = daoSymptom.findById(cui);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return symptom;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Symptom findByName(String symptomName) {
        return daoSymptom.findByNameQuery(symptomName);
    }

    @Override
    public boolean findHasSemanticTypeIdNative(String cui, String semanticType) {
        return daoSymptom.findHasSemanticTypeIdNative(cui, semanticType);
    }

    @Override
    public List<Object[]> findBySourceAndVersionNative(Date version, String source) {
        return daoSymptom.findBySourceAndVersionNative( version, source );
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Symptom> findAll() {
        List<Symptom> symptomList = daoSymptom.findAllQuery();
        return symptomList;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Symptom symptom) {
        daoSymptom.persist(symptom);
    }

    @Override
    public int insertNative(String cui, String name) {
        return daoSymptom.insertNative( cui, name );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertInBatch(List<Symptom> entityList) {
        return daoSymptom.insertInBatch(entityList);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Symptom symptom) {
        Symptom sy = daoSymptom.findById(symptom.getCui());
        if(sy!=null){
                sy.setCui( symptom.getCui() );
                sy.setName( symptom.getName() );
/*
            if(StringUtils.isNotBlank(code.getCodePK()))
                cod.setSourceId(code.getSourceId());
            if(StringUtils.isNotBlank(code.getName()))
                cod.setName(code.getName());
*/
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Symptom symptom) {
        Symptom sy = daoSymptom.findById(symptom.getCui());
        if(sy!=null){
            if(StringUtils.isNotBlank(symptom.getCui()))
                sy.setCui( symptom.getCui() );
            if(StringUtils.isNotBlank(symptom.getName()))
                sy.setName( symptom.getName() );
/*
            if(StringUtils.isNotBlank(code.getCodePK()))
                cod.setSourceId(code.getSourceId());
            if(StringUtils.isNotBlank(code.getName()))
                cod.setName(code.getName());
*/
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(String cui) {
        Symptom symptom = daoSymptom.findById(cui);
        if(symptom !=null)
            daoSymptom.delete(symptom);
        else
            return false;
        return true;
    }
}
