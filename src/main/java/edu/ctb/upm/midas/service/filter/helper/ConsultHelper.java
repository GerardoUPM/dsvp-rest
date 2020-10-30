package edu.ctb.upm.midas.service.filter.helper;

import edu.ctb.upm.midas.service.jpa.SourceService;
import edu.ctb.upm.midas.service.jpa.SymptomService;
import edu.ctb.upm.midas.service.jpa.TextService;
import edu.ctb.upm.midas.model.filter.common.Consult;
import edu.ctb.upm.midas.model.filter.common.query.ResponseSymptom;
import edu.ctb.upm.midas.model.filter.common.query.ResponseText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 20/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.ctb.upm.midas
 * @className ValidationHelper
 * @see
 */
@Component
public class ConsultHelper {

    @Autowired
    private SourceService sourceService;
    @Autowired
    private TextService textService;
    @Autowired
    private SymptomService symptomService;


    /**
     * @param consult
     * @return
     */
    public List<ResponseText> findTextsByVersionAndSource(Consult consult){
        List<ResponseText> responseTextList = new ArrayList<>();
        List<Object[]> texts = textService.findBySourceAndVersionNative(consult.getDate(), consult.getSource());
        int count = 1, call = 1;
        for (Object[] text: texts) {
            // Verificación del texto para evitar envíar textos vacíos a la api rest
            //if ( ((String) text[6]).isEmpty() ) continue;
            ResponseText responseText = new ResponseText();
            responseText.setSourceId((String) text[0]);
            responseText.setSourceName((String) text[1]);
            responseText.setDocumentId((String) text[2]);
            responseText.setVersion((Date) text[3]);
            responseText.setTextId((String) text[4]);
            responseText.setContentType((String) text[5]);
            responseText.setText((String) text[6]);

            responseTextList.add(responseText);

            if (count == call){
                responseText.setCall(true);
                call = call + 10;
            }else if (count == texts.size()) responseText.setCall(true);
            count++;
        }
        return responseTextList;
    }


    /**
     * @param consult
     * @return
     */
    public List<ResponseText> restartFindTextsByVersionAndSource(Consult consult){
        List<ResponseText> responseTextList = new ArrayList<>();
        //System.out.println(consult);
        List<Object[]> texts = textService.findByLikeVersionNative(consult.getDate(), consult.getSnapshot(), consult.getSource());
        int count = 1, call = 1;
        for (Object[] text: texts) {
            // Verificación del texto para evitar envíar textos vacíos a la api rest
            //if ( ((String) text[6]).isEmpty() ) continue;
            ResponseText responseText = new ResponseText();
            responseText.setSourceId((String) text[0]);
            responseText.setSourceName((String) text[1]);
            responseText.setDocumentId((String) text[2]);
            responseText.setVersion((Date) text[3]);
            responseText.setTextId((String) text[4]);
            responseText.setContentType((String) text[5]);
            responseText.setText((String) text[6]);

            responseTextList.add(responseText);

            if (count == call){
                responseText.setCall(true);
                call = call + 10;
            }else if (count == texts.size()) responseText.setCall(true);
            count++;
        }
        return responseTextList;
    }


    /**
     * @param consult
     * @return
     */
    public List<ResponseSymptom> findSymptomsByVersionAndSource(Consult consult){
        List<ResponseSymptom> responseSymptomList = new ArrayList<>();
        List<Object[]> symptoms = symptomService.findBySourceAndVersionNative(consult.getDate(), consult.getSource());
        for (Object[] symptom: symptoms) {
            ResponseSymptom responseSymptom = new ResponseSymptom();
            responseSymptom.setSourceId((String) symptom[0]);
            responseSymptom.setSourceName((String) symptom[1]);
            responseSymptom.setDocumentId((String) symptom[2]);
            responseSymptom.setVersion((Date) symptom[3]);
            responseSymptom.setTextId((String) symptom[4]);
            responseSymptom.setCui((String) symptom[5]);
            responseSymptom.setSymptomName((String) symptom[6]);
            responseSymptom.setValidated((boolean) symptom[7]);

            responseSymptomList.add( responseSymptom );
        }
        return responseSymptomList;
    }




    public String getDocumentId(String documentID, Date date ){
        return documentID +".V" + date;
    }


    /**
     * @param consult
     * @return
     */


}
