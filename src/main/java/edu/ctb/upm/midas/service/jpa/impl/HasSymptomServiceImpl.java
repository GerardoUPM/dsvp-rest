package edu.ctb.upm.midas.service.jpa.impl;
import edu.ctb.upm.midas.model.filter.common.Consult;
import edu.ctb.upm.midas.model.filter.metamap.response.Response;
import edu.ctb.upm.midas.model.jpa.HasSymptom;
import edu.ctb.upm.midas.model.jpa.HasSymptomPK;
import edu.ctb.upm.midas.repository.jpa.HasSymptomRepository;
import edu.ctb.upm.midas.service.jpa.HasSymptomService;
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
 * @className HasSymptomServiceImpl
 * @see
 */
@Service("HasSymptomService")
public class HasSymptomServiceImpl implements HasSymptomService {

    @Autowired
    private HasSymptomRepository daoHasSymptom;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public HasSymptom findById(HasSymptomPK hasSymptomPK) {
        HasSymptom hasSymptom = daoHasSymptom.findById(hasSymptomPK);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return hasSymptom;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<HasSymptom> findAll() {
        List<HasSymptom> hasSymptomList = daoHasSymptom.findAllQuery();
        return hasSymptomList;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(HasSymptom hasSymptom) {
        daoHasSymptom.persist(hasSymptom);
    }

    @Override
    public int insertNative(String textId, String cui, boolean validated, String matchedWords, String positionalInfo) {
        return daoHasSymptom.insertNative( textId, cui, validated, matchedWords, positionalInfo );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertInBatch(List<HasSymptom> entityList){
        return daoHasSymptom.insertInBatch(entityList);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(HasSymptom hasSymptom, HasSymptomPK hasSymptomPK) {
        HasSymptom hs = daoHasSymptom.findById( hasSymptomPK );
        if(hs!=null){
            //==>hs.setHasSymptomPK(hasSymptom.getHasSymptomPK());
            //==>hs.setValidated(hasSymptom.getValidated());
//            sour.set(source.getName());
            //sour.getDiseasesBySidsource().clear();
            //sour.getDiseasesBySidsource().addAll(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource())?source.getDiseasesBySidsource():new ArrayList<Disease>());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(HasSymptom hasSymptom, HasSymptomPK hasSymptomPK) {
        HasSymptom hs = daoHasSymptom.findById( hasSymptomPK );
        if(hs!=null){
            //==>if (hasSymptom.getHasSymptomPK()!=null)
            //==>    hs.setHasSymptomPK( hasSymptom.getHasSymptomPK() );
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
    public boolean deleteById(HasSymptomPK hasSymptomPK) {
        HasSymptom hasSymptom = daoHasSymptom.findById(hasSymptomPK);
        if(hasSymptom !=null)
            daoHasSymptom.delete(hasSymptom);
        else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int updateValidatedNative(String version, String sourceId, String cui, boolean validated) {
        return daoHasSymptom.updateValidatedNative(version, sourceId, cui, validated);
    }

    @Override
    public int updateMatchedWordsAndPositionalInfoNative(String textId, String cui, String matchedWords, String positionalInfo) {
        return daoHasSymptom.updateMatchedWordsAndPositionalInfoNative(textId, cui, matchedWords, positionalInfo);
    }
}
