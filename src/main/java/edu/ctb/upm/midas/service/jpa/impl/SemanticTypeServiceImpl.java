package edu.ctb.upm.midas.service.jpa.impl;
import edu.ctb.upm.midas.model.jpa.SemanticType;
import edu.ctb.upm.midas.repository.jpa.SemanticTypeRepository;
import edu.ctb.upm.midas.service.jpa.SemanticTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SemanticTypeServiceImpl
 * @see
 */
@Service
public class SemanticTypeServiceImpl implements SemanticTypeService {

    @Autowired
    private SemanticTypeRepository daoSemanticType;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public SemanticType findById(String semanticType) {
        SemanticType st = daoSemanticType.findById(semanticType);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return st;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public SemanticType findByDescriptionQuery(String semanticTypeName) {
        return daoSemanticType.findByDescriptionQuery(semanticTypeName);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<SemanticType> findAll() {
        List<SemanticType> semanticTypeList = daoSemanticType.findAllQuery();
        return semanticTypeList;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(SemanticType semanticType) {
        daoSemanticType.persist(semanticType);
    }

    @Override
    public int insertNative(String semanticType, String description) {
        return daoSemanticType.insertNative( semanticType, description );
    }

    @Override
    public int insertNativeHasSemanticType(String cui, String semanticType) {
        return daoSemanticType.insertNativeHasSemanticType( cui, semanticType );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertInBatch(List<SemanticType> entityList) {
        return daoSemanticType.insertInBatch(entityList);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(SemanticType semanticType) {
        SemanticType st = daoSemanticType.findById(semanticType.getSemanticType());
        if(st!=null){
                st.setSemanticType( semanticType.getSemanticType() );
                st.setDescription( semanticType.getDescription() );
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(SemanticType semanticType) {
        SemanticType st = daoSemanticType.findById(semanticType.getSemanticType());
        if(st!=null){
            //MODIFICAR SERIAMENTE
                //st.setSymptomList( semanticType.getSymptomList() );
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
    public boolean deleteById(String semanticType) {
        SemanticType semanticType1 = daoSemanticType.findById(semanticType);
        if(semanticType1 !=null)
            daoSemanticType.delete(semanticType1);
        else
            return false;
        return true;
    }
}
