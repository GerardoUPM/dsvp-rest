package edu.ctb.upm.midas.service.jpa.impl;
import edu.ctb.upm.midas.model.jpa.Text;
import edu.ctb.upm.midas.repository.jpa.TextRepository;
import edu.ctb.upm.midas.service.jpa.TextService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className TextServiceImpl
 * @see
 */
@Service("textService")
public class TextServiceImpl implements TextService {

    @Autowired
    private TextRepository daoText;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Text findById(String textId) {
        Text text = daoText.findById(textId);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return text;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Text findByContentTypeQuery(String contentType) {
        Text txt = daoText.findByContentTypeQuery(contentType);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return txt;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Text findByTextQuery(String text) {
        Text txt = daoText.findByTextQuery(text);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return txt;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Text> findAll() {
        List<Text> listTEXTEntities = daoText.findAllQuery();
        return listTEXTEntities;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Object[]> findBySourceAndVersionNative(Date version, String source) {
        return daoText.findBySourceAndVersionNative(version, source);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Object[]> findByLikeVersionNative(Date version, String strVersion, String source) {
        return daoText.findByLikeVersionNative(version, strVersion, source);
    }

    @Override
    public List<Object[]> findTextWithDetails(String source, Date snapshot, String textId) {
        return daoText.findTextWithDetails(source,snapshot, textId);
    }

    @Override
    public Integer getDisnetConceptsCountInAText(String sourceName, String snapshot, String textId, boolean validatedMedicalElement) {
        return daoText.getDisnetConceptsCountInAText(sourceName, snapshot, textId, validatedMedicalElement);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Text text) {
        daoText.persist(text);
    }

    @Override
    public int insertNative(String textId, String contentType, String text) {
        return daoText.insertNative( textId, contentType, text );
    }

    @Override
    public int insertNativeUrl(String textId, String urlId) {
        return daoText.insertNativeUrl( textId, urlId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Text text) {
        Text txt = daoText.findById(text.getTextId());
        if(txt!=null){
            if(StringUtils.isNotBlank(text.getTextId()))
                txt.setTextId(text.getTextId());
            if(StringUtils.isNotBlank(text.getText()))
                txt.setText(text.getText());
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Text text) {
        Text txt = daoText.findById(text.getTextId());
        if(txt!=null){
            if(StringUtils.isNotBlank(text.getTextId()))
                txt.setTextId(text.getTextId());
            if(StringUtils.isNotBlank(text.getText()))
                txt.setText(text.getText());
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(String textId) {
        Text text = daoText.findById(textId);
        if(text !=null)
            daoText.delete(text);
        else
            return false;
        return true;
    }
}
