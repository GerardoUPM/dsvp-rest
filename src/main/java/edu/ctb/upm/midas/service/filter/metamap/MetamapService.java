package edu.ctb.upm.midas.service.filter.metamap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ctb.upm.midas.client_modules.filter.metamap.api_response.impl.MetamapResourceServiceImpl;
import edu.ctb.upm.midas.client_modules.filter.metamap.component.Metamap;
import edu.ctb.upm.midas.common.util.ReplaceUTF8;
import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.common.util.UniqueId;
import edu.ctb.upm.midas.constants.Constants;
import edu.ctb.upm.midas.model.filter.common.Consult;
import edu.ctb.upm.midas.service.filter.helper.ConsultHelper;
import edu.ctb.upm.midas.model.filter.common.query.ResponseText;
import edu.ctb.upm.midas.model.filter.metamap.request.Configuration;
import edu.ctb.upm.midas.model.filter.metamap.request.Request;
import edu.ctb.upm.midas.model.filter.metamap.request.Text;
import edu.ctb.upm.midas.model.filter.metamap.response.Concept;
import edu.ctb.upm.midas.model.filter.metamap.response.ProcessedText;
import edu.ctb.upm.midas.model.filter.metamap.response.Response;
import edu.ctb.upm.midas.model.filter.metamap.special.HasSymptom;
import edu.ctb.upm.midas.model.filter.metamap.special.SemanticType;
import edu.ctb.upm.midas.model.filter.metamap.special.Symptom;
import edu.ctb.upm.midas.service.jpa.*;
import edu.ctb.upm.midas.service.jpa.helperNative.ConfigurationHelper;
import edu.ctb.upm.midas.service.jpa.helperNative.SymptomHelperNative;
import gov.nih.nlm.nls.metamap.Ev;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.*;

/**
 * Created by gerardo on 20/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className MetamapService
 * @see
 */
@Service
public class MetamapService {

    private static final Logger logger = LoggerFactory.getLogger(MetamapService.class);


    @Autowired
    private ConsultHelper consultHelper;
    @Autowired
    private SymptomHelperNative symptomHelperNative;
    @Autowired
    private MetamapResourceServiceImpl metamapResourceService;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private SymptomService symptomService;
    @Autowired
    private SemanticTypeService semanticTypeService;
    @Autowired
    private HasSemanticTypeService hasSemanticTypeService;
    @Autowired
    private HasSymptomService hasSymptomService;
    @Autowired
    private ConfigurationHelper configurationHelper;

    @Autowired
    private Metamap metamap;
    @Autowired
    private ReplaceUTF8 replaceUTF8;
    @Autowired
    private TimeProvider utilDate;
    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private Constants constants;



    public void filter(Consult consult) throws Exception {
        Request request = new Request();//VALIDAR CONSULT
        Configuration conf = new Configuration();
        List<Text> texts = new ArrayList<>();
        String sourceId = "";
        Date version = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        conf.setOptions("-y -R");
        List<String> sources = new ArrayList<>();
        sources.add("SNOMEDCT_US");
        conf.setSources(sources);
        conf.setSemanticTypes(Constants.SEMANTIC_TYPES_LIST);

        request.setConfiguration( conf );

        System.out.println("Get all texts by version and source...");
        List<ResponseText> responseTexts = consultHelper.findTextsByVersionAndSource( consult );
        System.out.println("size: " + responseTexts.size());
        if (responseTexts != null) {
            int countRT = 1;
            for (ResponseText responseText : responseTexts) {
                if (countRT == 1){
                    sourceId = responseText.getSourceId();
                    version = responseText.getVersion();
                }

                Text text = new Text();
                text.setId( responseText.getTextId() );
                text.setText( responseText.getText() );
                texts.add(text);
                //System.out.println(responseText.getTextId());
                //System.out.println("LLAMAR("+countRT+"): " + responseText.isCall());
                if (responseText.isCall()){
                    // Se agregan los textos hasta el momento

                    //request.setTextList( texts );
                    //System.out.println("textsList size is: " + texts.size() + " AND request.textList is:" + request.getTextList().size());

                    //System.out.println( gson.toJson( request ) );


                    // Se eliminan los textos hasta el momento para dar paso a los nuevos y no superar nunca envíos
                    // de mas de 300 elementos.
                    //request.getTextList().clear();
                    //texts.clear();
                }
                countRT++;
            }

            //<editor-fold desc="BLOQUE QUE LLAMA Y OBTIENE RESULTADOS DE LA API">
            request.setTextList( texts );
            request.setToken(Constants.TOKEN);

            System.out.println( "Connection_ with METAMAP API..." );
            System.out.println( "Founding medical concepts in a texts... please wait, this process can take from minutes to hours... " );
            Response response = metamapResourceService.filterTexts( request );
            System.out.println( "Texts Size request..." + request.getTextList().size());
            System.out.println( "Filter Texts Size response..." + response.getTextList().size() );
            response.setAuthorized(response.getTextList().size()>=request.getTextList().size());
            System.out.println("Authorization: "+ response.isAuthorized());

            if (response.isAuthorized()) {
                System.out.println("save metamap reponse...");
                writeJSONFile(gson.toJson(response), consult /*utilDate.dateFormatyyyyMMdd(version) utilDate.getNowFormatyyyyMMdd(), consult.getSource()*/);
                System.out.println("Insert symptoms starting...");
                System.out.println(request.getTextList().size());
                int count = 1;//VALIDAR
                if (response.getTextList() != null) {
                    for (edu.ctb.upm.midas.model.filter.metamap.response.Text filterText : response.getTextList()) {
                        System.out.println("TEXT_ID: " + filterText.getId() + " | CONCEPTS(" + filterText.getConcepts().size() + "): ");
                        int countSymptoms = 1;
                        for (edu.ctb.upm.midas.model.filter.metamap.response.Concept concept : filterText.getConcepts()) {
                            System.out.println("Concept{ cui: " + concept.getCui() + " name: " + concept.getName() + " semTypes:" + concept.getSemanticTypes().toString() + "}");
                            symptomHelperNative.insertIfExist(concept, filterText.getId());//text.getId()
                            countSymptoms++;
                        }
                        count++;
                    }
                    System.out.println("Insert symptoms ready!...");

                } else {
                    System.out.println("ERROR");
                /*System.out.println(gson.toJson( response ) );*/
                }
                //</editor-fold>

                System.out.println("Insert configuration...");
                String configurationJson = gson.toJson(request.getConfiguration());
                configurationHelper.insert(Constants.SOURCE_WIKIPEDIA, version, constants.SERVICE_METAMAP_CODE, configurationJson);
                //System.out.println("Insert configuration ready!...");
            }else{
                System.out.println("Authorization message: " + response.getAuthorizationMessage() + " | token: " + response.getToken());
            }
        }

    }


    /**
     *
     * @param consult
     * @return
     * @throws Exception
     */
    @Transactional
    public void localFilter(Consult consult) throws Exception {
        Request request = new Request();//VALIDAR CONSULT
        Configuration conf = new Configuration();
        List<Text> texts = new ArrayList<>();
        String sourceId = "";
        Date version = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        conf.setOptions("-y -R");
        List<String> sources = new ArrayList<>();
        sources.add("SNOMEDCT_US");
        conf.setSources(sources);
        conf.setSemanticTypes(Constants.SEMANTIC_TYPES_LIST);

        request.setConfiguration( conf );

        System.out.println("Get all texts by version and source...");
        List<ResponseText> responseTexts = consultHelper.findTextsByVersionAndSource( consult );
        System.out.println("size: " + responseTexts.size());
        if (responseTexts != null) {
            int countRT = 1;
            for (ResponseText responseText : responseTexts) {
                if (countRT == 1){
                    sourceId = responseText.getSourceId();
                    version = responseText.getVersion();
                }
                System.out.println("("+countRT+") Filter text: " + responseText.getTextId());
                String textNonAscii = replaceUTF8.replaceLooklike( responseText.getText() );

                if (!textNonAscii.isEmpty()){
                    for (Ev conceptEv : metamap.performNLP( textNonAscii ) ) {

                        Concept concept = new Concept();
                        concept.setCui( conceptEv.getConceptId() );
                        concept.setName( conceptEv.getConceptName() );
                        concept.setSemanticTypes( conceptEv.getSemanticTypes() );
                        concept.setMatchedWords(conceptEv.getMatchedWords());
                        concept.setPositionalInfo(conceptEv.getPositionalInfo().toString());

                        System.out.println( "   Insert symptom..." + concept.toString() );
                        symptomHelperNative.insertIfExist(concept, responseText.getTextId());
                    }// busqueda de conceptos con metamap
                }// validación del texto no vacío
                countRT++;
            }
        }

        if (!sourceId.isEmpty() && !version.toString().isEmpty()) {
            System.out.println("Insert configuration...");
            String configurationJson = gson.toJson(request.getConfiguration());
            configurationHelper.insert(Constants.SOURCE_WIKIPEDIA, version, constants.SERVICE_METAMAP_CODE, configurationJson);
            System.out.println("Insert configuration ready!...");
        }

    }


    /**
     *
     * @param consult
     * @return
     * @throws Exception
     */
    public void filterByParts(Consult consult) throws Exception {
        Request request = new Request();//VALIDAR CONSULT
        Configuration conf = new Configuration();
        List<Text> texts = new ArrayList<>();
        String sourceId = "";
        Date version = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        conf.setOptions("-y -R");
        List<String> sources = new ArrayList<>();
        sources.add("SNOMEDCT_US");
        conf.setSources(sources);
        conf.setSemanticTypes(Constants.SEMANTIC_TYPES_LIST);
        conf.setConcept_location(true);

        request.setConfiguration( conf );

        System.out.println("Get all texts by version and source from DB...");
        List<ResponseText> responseTexts = consultHelper.findTextsByVersionAndSource( consult );
        System.out.println("size: " + responseTexts.size());
        if (responseTexts != null) {
            int countRT = 1;
            //Se recorren todos los textos recuperados
            for (ResponseText responseText : responseTexts) {
                if (countRT == 1){
                    sourceId = responseText.getSourceId();
                    version = responseText.getVersion();
                }
                //SE generan objetos textos para despues ser procesados
                Text text = new Text();
                text.setId( responseText.getTextId() );
                text.setText( responseText.getText() );
                if (countRT == 1){
                    text.setId( "MyIDGLG");
                    text.setText( "Fever" );
                }
                texts.add(text);

                //System.out.println(responseText.getTextId());
                System.out.println(countRT+". CALL " + responseText.isCall() + " FROM: " + responseTexts.size() );
                //isCall=true indica cuando se llamará al servicio Web de Metamap
                if (responseText.isCall()){
                    // Se agregan los textos hasta el momento
                    //System.out.println( gson.toJson( request ) );
                    System.out.println("");
                    //<editor-fold desc="BLOQUE QUE LLAMA Y OBTIENE RESULTADOS DE LA API">
                    request.setTextList( texts );
                    request.setToken(Constants.TOKEN);

                    //request.setTextList( texts );
                    System.out.println("textsList size is: " + texts.size() + " AND request.textList is:" + request.getTextList().size());

                    System.out.println( "Connection_ with METAMAP API..." );
                    System.out.println( "Founding medical concepts in a texts... please wait, this process can take from minutes to hours... " );
                    //Se llama a la METAMAP REST API
                    Response response = metamapResourceService.filterTexts( request );
                    System.out.println( "Texts Size request..." + request.getTextList().size());
                    System.out.println( "Filter Texts Size response..." + response.getTextList().size() );
                    response.setAuthorized(response.getTextList().size()>=request.getTextList().size());
                    System.out.println("Authorization: "+ response.isAuthorized());

                    if (response.isAuthorized()) {

                        System.out.println("Insert symptoms starting...");
                        System.out.println(request.getTextList().size());
                        int count = 1;//VALIDAR
                        if (response.getTextList() != null) {
                            for (edu.ctb.upm.midas.model.filter.metamap.response.Text filterText : response.getTextList()) {
                                System.out.println("TEXT_ID: " + filterText.getId() + " | CONCEPTS(" + filterText.getConcepts().size() + "): ");
                                int countSymptoms = 1;
                                for (edu.ctb.upm.midas.model.filter.metamap.response.Concept concept : filterText.getConcepts()) {
                                    System.out.println("Concept{ cui: " + concept.getCui() + " name: " + concept.getName() + " semTypes:" + concept.getSemanticTypes().toString() + "Position: " + concept.getMatchedWords() + "}");
                                    symptomHelperNative.insertIfExist(concept, filterText.getId());//text.getId()
                                    System.out.println("Concept insert ready!");
                                    countSymptoms++;
                                }
                                count++;
                            }
                            System.out.println("Insert symptoms ready!...");

                        } else {
                            System.out.println("ERROR");
                /*System.out.println(gson.toJson( response ) );*/
                        }
                        //</editor-fold>


                    }else{
                        System.out.println("Authorization message: " + response.getAuthorizationMessage() + " | token: " + response.getToken());
                    }


                    // Se eliminan los textos hasta el momento para dar paso a los nuevos y no superar nunca envíos
                    // de mas de 300 elementos.
                    request.getTextList().clear();
                    texts.clear();
                }
                countRT++;
            }


            System.out.println("Insert configuration...");
            String configurationJson = gson.toJson(request.getConfiguration());
            configurationHelper.insert(consult.getSource(), sourceId, version, constants.SERVICE_METAMAP_CODE, configurationJson);
            //System.out.println("Insert configuration ready!...");



        }

    }



    /**
     *
     * @param consult
     * @return
     * @throws Exception
     */


    public void filterAndStorageInJSON(Consult consult) throws Exception {
        Request request = new Request();//VALIDAR CONSULT
        Configuration conf = new Configuration();
        List<Text> texts = new ArrayList<>();
        String sourceId = "";
        Date version = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        conf.setOptions("-y -R");
        List<String> sources = new ArrayList<>();
        sources.add("SNOMEDCT_US");
        //sources.add("DSM-5");
        conf.setSources(sources);
        conf.setSemanticTypes(Constants.SEMANTIC_TYPES_LIST);
        conf.setConcept_location(true);
        conf.setRefreshSessionCount(0);

        request.setConfiguration( conf );
        request.setSnapshot(consult.getSnapshot());
        request.setSource(consult.getSource());

        logger.info("Get all texts by source and snapshot...");
        List<ResponseText> responseTexts = consultHelper.findTextsByVersionAndSource( consult );
        logger.info("Size of the text list: " + responseTexts.size());
        if (responseTexts.size()>0) {
            int countRT = 1;
            for (ResponseText responseText : responseTexts) {
                if (countRT == 1){
                    sourceId = responseText.getSourceId();
                    version = responseText.getVersion();
                }
                Text text = new Text();
                text.setId( responseText.getTextId() );
                text.setText( responseText.getText() );
                texts.add(text);
                //System.out.println(responseText.getTextId());
                //System.out.println("LLAMAR("+countRT+"): " + responseText.isCall());
                if (responseText.isCall()){
                    // Se agregan los textos hasta el momento

                    //request.setTextList( texts );
                    //System.out.println("textsList size is: " + texts.size() + " AND request.textList is:" + request.getTextList().size());

                    //System.out.println( gson.toJson( request ) );


                    // Se eliminan los textos hasta el momento para dar paso a los nuevos y no superar nunca envíos
                    // de mas de 300 elementos.
                    //request.getTextList().clear();
                    //texts.clear();
                }
                countRT++;
            }

            //<editor-fold desc="BLOQUE QUE LLAMA Y OBTIENE RESULTADOS DE LA API">
            request.setTextList( texts );
            request.setToken(Constants.TOKEN);

            //System.out.println( "Request: " + request);
            logger.info("Connection_ with METAMAP API..." );
            logger.info( "Founding medical concepts in a texts... please wait, this process can take from minutes to hours (about 24hrs for " + consult.getSource() + ")... " );
            Response response = metamapResourceService.filterTexts( request );
            logger.info( "Texts Size request..." + request.getTextList().size());
            logger.info( "Filter Texts Size response..." + response.getTextList().size() );
            response.setAuthorized(response.getTextList().size()>=request.getTextList().size());
            logger.info("Authorization: "+ response.isAuthorized());

            if (response.isAuthorized()) {
//                logger.info("save metamap reponse...");
//                ProcessedText processedText = new ProcessedText();
//                processedText.setTexts(response.getTextList());
//                writeJSONFile(gson.toJson(processedText), utilDate.dateFormatyyyyMMdd(version));
//                writeJSONFile(gson.toJson(response), consult /*utilDate.dateFormatyyyyMMdd(version) utilDate.getNowFormatyyyyMMdd()*/);//response => cambio para almacenar la configuración con la cual se ejecuto metamap
//                System.out.println("save metamap ready...");

                System.out.println("Insert MetaMap Configuration...");
                String configurationJson = gson.toJson(request.getConfiguration());
                configurationHelper.insert(consult.getSource(), sourceId, version, constants.SERVICE_METAMAP_CODE, configurationJson);
            }else{
                System.out.println("Authorization message: " + response.getAuthorizationMessage() + " | token: " + response.getToken());
            }
        }

    }


    public void insertInBatchMedicalTerms(Response response, Date version, Gson gson){
        List<edu.ctb.upm.midas.model.filter.metamap.response.Text> textList = response.getTextList();
        if (textList.size()>0) {
            System.out.println("Texts Size request..." + textList.size());
            System.out.println("Filter Texts Size response..." + textList.size());

            System.out.println("Insert symptoms starting...");
            String queryHead = "INSERT IGNORE INTO has_symptom (text_id, cui, validated, matched_words, positional_info) VALUES ";
            int count = 1;//VALIDAR
            for (edu.ctb.upm.midas.model.filter.metamap.response.Text text : textList) {
                System.out.println(count + ". to (" + textList.size() + ") TEXT_ID: " + text.getId() + " | CONCEPTS(" + text.getConcepts().size() + "): ");
                List<Concept> noRepeatedConcepts = removeRepetedConcepts(text.getConcepts());

                int countSymptoms = 1;
                for (edu.ctb.upm.midas.model.filter.metamap.response.Concept concept : text.getConcepts()) {
                    System.out.println("Concept{ cui: " + concept.getCui() + " name: " + concept.getName() + " semTypes:" + concept.getSemanticTypes().toString() + "}");
//                    symptomHelperNative.insertIfExist(concept, text.getId());//text.getId()
                    countSymptoms++;
                }
                count++;
            }
            System.out.println("Insert symptoms ready!...");
            //</editor-fold>

            System.out.println("Insert configuration...");
            String configurationJson = gson.toJson(response.getConfiguration());
            configurationHelper.insert(Constants.SOURCE_WIKIPEDIA, version, constants.SERVICE_METAMAP_CODE, configurationJson);
            System.out.println("Insert configuration ready!...");
        } else {
            System.out.println("Texts Size Different: request: " + response.getTextList().size() + " | json: " + textList.size());
        }
    }


    /**
     *
     * @param consult
     * @return
     * @throws Exception
     */
    public void populateTextsStoredJSON(Consult consult) throws Exception {
        Request request = new Request();//VALIDAR CONSULT
        Configuration conf = new Configuration();
        List<Text> texts = new ArrayList<>();
        String sourceId = "";
        Date version = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        conf.setOptions("-y -R");
        List<String> sources = new ArrayList<>();
        sources.add("SNOMEDCT_US");
        conf.setSources(sources);
        conf.setSemanticTypes(Constants.SEMANTIC_TYPES_LIST);
        conf.setConcept_location(true);
        request.setSnapshot(consult.getSnapshot());
        request.setSource(consult.getSource());

        request.setConfiguration( conf );

        System.out.println("Get all texts by version and source...");
        List<ResponseText> responseTexts = consultHelper.findTextsByVersionAndSource( consult );
        System.out.println("size: " + responseTexts.size());
        if (responseTexts != null) {
            int countRT = 1;
            for (ResponseText responseText : responseTexts) {
                if (countRT == 1){
                    sourceId = responseText.getSourceId();
                    version = responseText.getVersion();
                }

                Text text = new Text();
                text.setId( responseText.getTextId() );
                text.setText( responseText.getText() );
                texts.add(text);
                //System.out.println(responseText.getTextId());
                //System.out.println("LLAMAR("+countRT+"): " + responseText.isCall());
                if (responseText.isCall()){
                    // Se agregan los textos hasta el momento

                    //request.setTextList( texts );
                    //System.out.println("textsList size is: " + texts.size() + " AND request.textList is:" + request.getTextList().size());

                    //System.out.println( gson.toJson( request ) );


                    // Se eliminan los textos hasta el momento para dar paso a los nuevos y no superar nunca envíos
                    // de mas de 300 elementos.
                    //request.getTextList().clear();
                    //texts.clear();
                }
                countRT++;
            }

            //<editor-fold desc="BLOQUE QUE LLAMA Y OBTIENE RESULTADOS DE LA API">
            request.setTextList( texts );
            request.setToken(Constants.TOKEN);

            System.out.println( "Connection_ with METAMAP API..." );
            System.out.println( "Founding medical concepts in a texts... please wait, this process can take from minutes to hours... " );
            Response response = readMetamapResponseJSON(consult, false);

            if (response!=null) {
                List<edu.ctb.upm.midas.model.filter.metamap.response.Text> textList = response.getTextList();
                if (textList.size() == request.getTextList().size()) {
                    System.out.println("Texts Size request..." + request.getTextList().size());
                    System.out.println("Filter Texts Size response..." + textList.size());

                    System.out.println("Insert symptoms starting...");
                    int count = 1;//VALIDAR
                    for (edu.ctb.upm.midas.model.filter.metamap.response.Text filterText : textList) {
                        System.out.println(count + ". to (" + textList.size() + ") TEXT_ID: " + filterText.getId() + " | CONCEPTS(" + filterText.getConcepts().size() + "): ");
                        int countSymptoms = 1;
                        for (edu.ctb.upm.midas.model.filter.metamap.response.Concept concept : filterText.getConcepts()) {
                            System.out.println("Concept{ cui: " + concept.getCui() + " name: " + concept.getName() + " semTypes:" + concept.getSemanticTypes().toString() + "}");
                            symptomHelperNative.insertIfExist(concept, filterText.getId());//text.getId()
                            countSymptoms++;
                        }
                        count++;
                    }
                    System.out.println("Insert symptoms ready!...");
                    //</editor-fold>

                    System.out.println("Insert configuration...");
                    String configurationJson = gson.toJson(request.getConfiguration());
                    configurationHelper.insert(Constants.SOURCE_WIKIPEDIA, version, constants.SERVICE_METAMAP_CODE, configurationJson);
                    System.out.println("Insert configuration ready!...");
                } else {
                    System.out.println("Texts Size Different: request: " + request.getTextList().size() + " | json: " + textList.size());
                }
            }
        }


    }


    /**
     *
     * @param consult
     * @return
     * @throws Exception
     */
    public void restartPopulateTextsStoredJSON(Consult consult) throws Exception {
        Request request = new Request();//VALIDAR CONSULT
        Configuration conf = new Configuration();
        List<Text> texts = new ArrayList<>();
        String sourceId = "";
        Date version = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        conf.setOptions("-y -R");
        List<String> sources = new ArrayList<>();
        sources.add("SNOMEDCT_US");
        conf.setSources(sources);
        conf.setSemanticTypes(Constants.SEMANTIC_TYPES_LIST);
        conf.setConcept_location(true);
        request.setSnapshot(consult.getSnapshot());
        request.setSource(consult.getSource());

        request.setConfiguration( conf );

        System.out.println("Get all texts by version and source...");
        List<ResponseText> responseTexts = consultHelper.restartFindTextsByVersionAndSource( consult );
        System.out.println("size: " + responseTexts.size());
        if (responseTexts != null) {
            int countRT = 1;
            for (ResponseText responseText : responseTexts) {
                if (countRT == 1){
                    sourceId = responseText.getSourceId();
                    version = responseText.getVersion();
                }

                Text text = new Text();
                text.setId( responseText.getTextId() );
                text.setText( responseText.getText() );
                texts.add(text);
                //System.out.println(responseText.getTextId());
                //System.out.println("LLAMAR("+countRT+"): " + responseText.isCall());
                if (responseText.isCall()){
                    // Se agregan los textos hasta el momento

                    //request.setTextList( texts );
                    //System.out.println("textsList size is: " + texts.size() + " AND request.textList is:" + request.getTextList().size());

                    //System.out.println( gson.toJson( request ) );


                    // Se eliminan los textos hasta el momento para dar paso a los nuevos y no superar nunca envíos
                    // de mas de 300 elementos.
                    //request.getTextList().clear();
                    //texts.clear();
                }
                countRT++;
            }

            //<editor-fold desc="BLOQUE QUE LLAMA Y OBTIENE RESULTADOS DE LA API">
            request.setTextList( texts );//resultado de la consulta
            request.setToken(Constants.TOKEN);

            System.out.println( "Connection_ with METAMAP API..." );
            System.out.println( "Founding medical concepts in a texts... please wait, this process can take from minutes to hours... " );
            Response response = readMetamapResponseJSON(consult, false);

            if (response!=null) {
                List<edu.ctb.upm.midas.model.filter.metamap.response.Text> textList = response.getTextList();
                if (textList.size() == request.getTextList().size()) {
                    System.out.println("Texts Size request..." + request.getTextList().size());
                    System.out.println("Filter Texts Size response..." + textList.size());
                    int insertados = 1, noinsertados = 1;
                    System.out.println("Insert symptoms starting...");
                    int count = 1;//VALIDAR
                    for (edu.ctb.upm.midas.model.filter.metamap.response.Text filterText : textList) {
                        //Verifica que se encuentre en la lista de los textos
                        if (contains(texts, filterText.getId())) {
                            insertados++;
                            System.out.println(count + ". to (" + textList.size() + ") 'Inserta' TEXT_ID: " + filterText.getId() + " | CONCEPTS(" + filterText.getConcepts().size() + "): ");
                            int countSymptoms = 1;
                            for (edu.ctb.upm.midas.model.filter.metamap.response.Concept concept : filterText.getConcepts()) {
                                System.out.println("Concept{ cui: " + concept.getCui() + " name: " + concept.getName() + " semTypes:" + concept.getSemanticTypes().toString() + "}");
                                symptomHelperNative.insertIfExist(concept, filterText.getId());//text.getId()
                                countSymptoms++;
                            }
                        } else {
                            noinsertados++;
                            System.out.println(count + ". to (" + textList.size() + ") Ya insertado textId: " + filterText.getId());
                        }
                        count++;
                        //if (count==18000) break;
                    }
                    System.out.println("Insert symptoms ready!...");
                    //</editor-fold>

                    System.out.println("Insert configuration...");
                    String configurationJson = gson.toJson(request.getConfiguration());
                    configurationHelper.insert(Constants.SOURCE_WIKIPEDIA, version, constants.SERVICE_METAMAP_CODE, configurationJson);
                    System.out.println("Insert configuration ready!...");
                    System.out.println("insertados: " + insertados + " noinsertados: " + noinsertados);
                }
            }
        }

    }


    public boolean contains(final List<Text> texts, String textId){
        return texts.stream()
                .filter(o -> o.getId().trim() != null)
                .filter(o -> o.getId().trim().contentEquals(textId.trim()))
                .findFirst()
                .isPresent();
                //return texts.stream().anyMatch(o -> Objects.equals(o.getId().trim(), textId.trim()));
        /*boolean res = false;
        for (Text text: texts) {
            if (text.getId().trim().equals(textId.trim())){
                //System.out.println(text.getId() +"=="+textId.trim());
                res = true; break;}
        }
        return res;*/
    }

    public void createMySQLInserts_TEST(Consult consult) throws Exception {
        Response response = readMetamapResponseJSON(consult, false);
        List<edu.ctb.upm.midas.model.filter.metamap.response.Text> textList = response.getTextList();
        System.out.println("TextList: " + textList.size());
        int count = 0;
        for (edu.ctb.upm.midas.model.filter.metamap.response.Text text:textList) {
            for (Concept concept: text.getConcepts()){
                count = count + 1;
            }
        }
        System.out.println("Número de conceptos: "+count);
    }

    @Transactional
    public void createMySQLInserts(Consult consult) throws Exception {
        final int batchSize = 500;

        List<SemanticType> semanticTypes = new ArrayList<>();
        List<Symptom> symptoms = new ArrayList<>();
        List<HasSymptom> hasSymptoms = new ArrayList<>();

        String fileNameHasSymptoms = consult.getSnapshot() + "_" + consult.getSource() + "_inserts_has_symptom.sql";
        String fileNameSemType = consult.getSnapshot() + "_" + consult.getSource() + "_inserts_semantic_types.sql";
        String fileNameSymptoms = consult.getSnapshot() + "_" + consult.getSource() + "_inserts_symptoms.sql";
        String fileHasSemTypes = consult.getSnapshot() + "_" + consult.getSource() + "_inserts_has_semantic_types.sql";
        String pathHasSymptoms = Constants.METAMAP_FOLDER + fileNameHasSymptoms;
        String pathSemTypes = Constants.METAMAP_FOLDER + fileNameSemType;
        String pathSymptoms = Constants.METAMAP_FOLDER + fileNameSymptoms;
        String pathHasSemTypes = Constants.METAMAP_FOLDER + fileHasSemTypes;
        FileWriter fileWriterHasSymptoms = new FileWriter(pathHasSymptoms);
        FileWriter fileWriterSemTypes = new FileWriter(pathSemTypes);
        FileWriter fileWriterSymptoms = new FileWriter(pathSymptoms);
        FileWriter fileWriteHasSemTypes = new FileWriter(pathHasSemTypes);

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        //Metamap configuración
//        Configuration metamapConf = new Configuration();
//        metamapConf.setOptions("-y -R");
//        List<String> sources = new ArrayList<>();
//        sources.add("SNOMEDCT_US");
//        metamapConf.setSources(sources);
//        metamapConf.setSemanticTypes(Constants.SEMANTIC_TYPES_LIST);
//        metamapConf.setConcept_location(true);

        Response response = readMetamapResponseJSON(consult, false);
        System.out.println("Read JSON ready!");
        String has_symptoms_inserts = "";

        if (response!=null) {
            List<edu.ctb.upm.midas.model.filter.metamap.response.Text> textList = response.getTextList();
            if (textList.size()>0) {
                try {
                    int textCount = 1, conceptCount = 1, countTotalHasSymptom = countHasSymptom(textList);
                    fileWriterHasSymptoms.write("INSERT IGNORE INTO has_symptom (text_id, cui, validated, matched_words, positional_info) VALUES ");
                    for (edu.ctb.upm.midas.model.filter.metamap.response.Text metamapText : textList) {
                        System.out.println(textCount + ". to " + textList.size() + " TextId: " + metamapText.getId());
                        //Validar que haya conceptos
                        if (metamapText.getConcepts() != null) {
                            //Al menos un concepto
                            if (metamapText.getConcepts().size() > 0) {
                                //List<Concept> conceptsAux = metamapText.getConcepts();
                                List<Concept> noRepeatedConcepts = removeRepetedConcepts(metamapText.getConcepts());
                                conceptCount = createHasSymptom(metamapText.getConcepts(), noRepeatedConcepts, hasSymptoms, metamapText.getId(), conceptCount, countTotalHasSymptom, fileWriterHasSymptoms);
                                for (Concept concept : metamapText.getConcepts()) {
                                    /*if (!isInvalidatedSemanticType(concept.getSemanticTypes())) {//validar para no insertar "clna",     "qlco",     "hcpp"*/
                                        //Se crea un sintoma
                                        Symptom symptom = new Symptom(concept.getCui(), concept.getName(), concept.getSemanticTypes());
                                        //Se agrega a la lista
                                        symptoms.add(symptom);

                                        //Se recorren los semantic types del concepto
                                        for (String semanticType : concept.getSemanticTypes()) {
                                            SemanticType semType = new SemanticType(semanticType);
                                            //Se crea la lista de semantic types
                                            semanticTypes.add(semType);
                                        }
                                        //Se elimina el elemento de la lista principal para no contarlo y no agregarlo al hacer merge
                                        //metamapText.getConcepts().remove(concept);
                                    /*}*/
                                }
                                //if (textCount == 50) break;
                                textCount++;
                            }
                        }
                    }
                    System.out.println("conceptCount: "+conceptCount+", countTotalHasSymptom: "+countTotalHasSymptom);
//                    fileWriterHasSymptoms.write(Constants.COMMA_DOT);
                    fileWriterHasSymptoms.close();
                } catch (Exception e) {
                    System.out.println("Mensaje de la excepción 2: " + e.getMessage());
                }
            }
        }

        //Eliminar repetidos
        //Tipos semanticos <<formar los insert para insertar semantics types "semantic_type">>
        System.out.println("SemanticTypes repetidos size: " + semanticTypes.size());
        semanticTypes = removeRepetedSemanticTypes(semanticTypes);
        System.out.println("SemanticTypes sin repetir size: " + semanticTypes.size());
        //formar inserts
        try {
            int counST = 1;
            fileWriterSemTypes.write("INSERT IGNORE INTO semantic_type (semantic_type, description) VALUES \n");
            for (SemanticType semanticType : semanticTypes) {
                //INSERT IGNORE INTO symptom (cui, name) VALUES ('C0231418', "At risk for violence");INSERT IGNORE INTO has_semantic_type (cui, semantic_type) VALUES ('C0231418', 'fndg');
                fileWriterSemTypes.write("('"+semanticType.getType()+"', '')" + (counST==semanticTypes.size()?Constants.COMMA_DOT:Constants.COMMA+"\n"));
                counST++;
            }
            fileWriterSemTypes.close();
        }catch (Exception e){
            System.out.println("Mensaje de la excepción 4: " + e.getMessage());
        }

        //Sintomas <<formar los insert para insertar sintomas "symptom" y sus tipos semanticos "has_semantic_type">>
        System.out.println("symptoms repetidos size: " + symptoms.size());
        symptoms = removeRepetedSymptoms(symptoms);
        System.out.println("symptoms sin repetir size: " + symptoms.size());
        //formar inserts para los sintomas y sus tipos semanticos
        try {
            int countS = 1, countHasSemType = 1, countHasSemTypes = countHasSemTypes(symptoms);
            fileWriterSymptoms.write("INSERT IGNORE INTO symptom (cui, name) VALUES \n");
            fileWriteHasSemTypes.write("INSERT IGNORE INTO has_semantic_type (cui, semantic_type) VALUES \n");
            for (Symptom symptom : symptoms) {
                fileWriterSymptoms.write("('"+symptom.getCui()+"', \""+symptom.getName()+"\")" + (countS==symptoms.size()?Constants.COMMA_DOT:Constants.COMMA+"\n"));
                for (String semType: symptom.getSemanticTypes()) {
                    fileWriteHasSemTypes.write("('" + symptom.getCui() + "', '" + semType + "')" + (countHasSemType==countHasSemTypes?Constants.COMMA_DOT:Constants.COMMA+"\n"));
                    countHasSemType++;
                }
                countS++;
            }
            fileWriterSymptoms.close();
//            fileWriteHasSemTypes.write(Constants.COMMA_DOT);
            fileWriteHasSemTypes.close();
        }catch (Exception e){
            System.out.println("Mensaje de la excepción 5: " + e.getMessage());
        }

        //HasSymptoms resultado del proceso de metamap en la tabla "has_symptom"
        System.out.println("has_symptoms size: " + hasSymptoms.size());


        //insertar configuración
//        System.out.println("Insert configuration...");
//        String configurationJson = gson.toJson(metamapConf);
//        configurationHelper.insert(consult.getSource(), consult.getDate(), constants.SERVICE_METAMAP_CODE, configurationJson);

    }


    public void insertInBatchOK(Consult consult){
        Response response = null;
        try {
            response = readMetamapResponseJSON(consult, false);
        }catch (Exception e) {
            logger.error("Error to read the MetaMap JSON");
        }


    }


    @Transactional
    public void insertInBatch(Consult consult) throws Exception {
        final int batchSize = 500;

        List<edu.ctb.upm.midas.model.jpa.SemanticType> semanticTypes = new ArrayList<>();
        List<edu.ctb.upm.midas.model.jpa.Symptom> symptoms = new ArrayList<>();
        List<edu.ctb.upm.midas.model.jpa.HasSymptom> hasSymptoms = new ArrayList<>();
        List<edu.ctb.upm.midas.model.jpa.HasSemanticType> hasSemTypes = new ArrayList<>();

        List<Symptom> symptomsAux = new ArrayList<>();

        List<String> semanticTypesInsertElements = new ArrayList<>();
        List<String> symptomsInsertElements = new ArrayList<>();
        List<String> hasSemanticTypesInsertElements = new ArrayList<>();
        List<String> hasSymptomInsertElements = new ArrayList<>();


        Response response = readMetamapResponseJSON(consult, false);
        System.out.println("Read JSON ready!");
        String has_symptoms_inserts = "";

        if (response!=null) {
            List<edu.ctb.upm.midas.model.filter.metamap.response.Text> textList = response.getTextList();
            if (textList.size()>0) {
                try {
                    int textCount = 1, conceptCount = 1, countTotalHasSymptom = countHasSymptom(textList);
//                    String headHasSymptoms = "INSERT IGNORE INTO has_symptom (text_id, cui, validated, matched_words, positional_info) VALUES ";
                    for (edu.ctb.upm.midas.model.filter.metamap.response.Text metamapText : textList) {
//                        System.out.println(textCount + ". to " + textList.size() + " TextId: " + metamapText.getId());
                        //Validar que haya conceptos
                        if (metamapText.getConcepts() != null) {
                            //Al menos un concepto
                            if (metamapText.getConcepts().size() > 0) {
                                //List<Concept> conceptsAux = metamapText.getConcepts();
                                List<Concept> noRepeatedConcepts = removeRepetedConcepts(metamapText.getConcepts());
                                conceptCount = createHasSymptomElement(metamapText.getConcepts(), noRepeatedConcepts, hasSymptoms, metamapText.getId(), conceptCount, countTotalHasSymptom, hasSymptomInsertElements);
                                for (Concept concept : metamapText.getConcepts()) {
                                    /*if (!isInvalidatedSemanticType(concept.getSemanticTypes())) {//validar para no insertar "clna",     "qlco",     "hcpp"*/
                                    //Se crea un sintoma
//                                    edu.ctb.upm.midas.model.jpa.Symptom existSymp = null;
//                                    existSymp = symptomService.findById(concept.getCui());

//                                    if (existSymp==null){
                                        edu.ctb.upm.midas.model.jpa.Symptom symptom = new edu.ctb.upm.midas.model.jpa.Symptom(concept.getCui(), concept.getName());
                                        //Se agrega a la lista
                                        symptoms.add(symptom);
//                                    }
                                    // Symps Aux
                                    Symptom symp = new Symptom(concept.getCui(), concept.getName(), concept.getSemanticTypes());
                                    symptomsAux.add(symp);

                                    //Se recorren los semantic types del concepto
                                    for (String semanticType : concept.getSemanticTypes()) {
                                        edu.ctb.upm.midas.model.jpa.SemanticType semType = new edu.ctb.upm.midas.model.jpa.SemanticType(semanticType);
                                        //Se crea la lista de semantic types
                                        semanticTypes.add(semType);
                                    }
                                    //Se elimina el elemento de la lista principal para no contarlo y no agregarlo al hacer merge
                                    //metamapText.getConcepts().remove(concept);
                                    /*}*/
                                }
                                //if (textCount == 50) break;
                                textCount++;
                            }
                        }
                    }
                    System.out.println("conceptCount: "+conceptCount+", countTotalHasSymptom: "+countTotalHasSymptom);
                } catch (Exception e) {
                    System.out.println("Mensaje de la excepción 2: " + e.getMessage());
                }
            }
        }

        //Eliminar repetidos
        //Tipos semanticos <<formar los insert para insertar semantics types "semantic_type">>
        System.out.println("SemanticTypes repetidos size: " + semanticTypes.size());
        semanticTypes = removeRepetedSemanticTypesJpa(semanticTypes);
        System.out.println("SemanticTypes sin repetir size: " + semanticTypes.size());
//        for (edu.ctb.upm.midas.model.jpa.SemanticType semanticType: semanticTypes) {
//            edu.ctb.upm.midas.model.jpa.SemanticType existST = null;
//            existST = semanticTypeService.findById(semanticType.getSemanticType());
//            if (existST==null) semanticTypeService.insertNative(semanticType.getSemanticType(), semanticType.getDescription());
//        }

        //Sintomas <<formar los insert para insertar sintomas "symptom" y sus tipos semanticos "has_semantic_type">>
        System.out.println("symptoms repetidos size: " + symptoms.size());
        symptoms = removeRepetedSymptomsJpa(symptoms);
        System.out.println("symptoms sin repetir size: " + symptoms.size());
        edu.ctb.upm.midas.model.jpa.Symptom existS = null;
        for (edu.ctb.upm.midas.model.jpa.Symptom symp: symptoms) {
            existS = symptomService.findById(symp.getCui());
            if (existS==null) {
//                System.out.println("NUEVO SINTOMA: " + symp.getCui() + " | " + symp.getName());
                symptomService.insertNative(symp.getCui(), symp.getName());
            }
        }

        symptomsAux = removeRepetedSymptoms(symptomsAux);
        //formar inserts para los sintomas y sus tipos semanticos
        try {
            int countS = 1, countHasSemType = 1, countHasSemTypes = countHasSemTypes(symptomsAux);
            edu.ctb.upm.midas.model.jpa.SemanticType existST = null;
//            for (Symptom symptom : symptomsAux) {
//                for (String semType: symptom.getSemanticTypes()) {
//                    existST = semanticTypeService.findById(semType);
//                    if (existST==null) semanticTypeService.insertNative(semType, "");
//
////                    HasSemanticType hasSemanticType = new HasSemanticType(symptom.getCui(), semType);
////                    hasSemTypes.add(hasSemanticType);
//                    countHasSemType++;
//                }
//                countS++;
//            }
        }catch (Exception e){
            System.out.println("Mensaje de la excepción 5: " + e.getMessage());
        }


        //HasSymptoms resultado del proceso de metamap en la tabla "has_symptom"
        System.out.println("semantic_types size: " + semanticTypes.size());
        System.out.println("symptoms size: " + symptoms.size());
        System.out.println("has_semantic_types size: " + hasSemTypes.size());
        System.out.println("has_symptoms size: " + hasSymptoms.size());

//        //es necesario quitar los que ya estan en la base de datos
//        logger.info("START batch symptoms insert");
//        symptomService.insertInBatch(symptoms);
//        logger.info("END batch symptoms insert");

//        logger.info("START batch symptoms insert");
//        semanticTypeService.insertInBatch(semanticTypes);
//        logger.info("END batch symptoms insert");
//
//        logger.info("START batch has semantic types insert");
//        hasSemanticTypeService.insertInBatch(hasSemTypes);
//        logger.info("END batch has semantic types insert");
//
//        logger.info("START batch has symptom insert");
//        hasSymptomService.insertInBatch(hasSymptoms);
//        logger.info("END batch has symptom insert");

//        logger.info("START batch has symptom insert");
//        int cHsympt = 1;
//        for (edu.ctb.upm.midas.model.jpa.HasSymptom hasSymptom: hasSymptoms) {
////            hasSymptomService.insertNative(hasSymptom.getTextId()
////                    , hasSymptom.getCui()
////                    ,hasSymptom.getValidated()!=0
////                    , hasSymptom.getMatchedWords()
////                    , hasSymptom.getPositionalInfo());
//            if (hasSymptom.getPositionalInfo().length()>=500)
//                logger.info(cHsympt + " to " + hasSymptoms.size() + " => " + hasSymptom.getTextId() + " - " + hasSymptom.getCui() + " - " + hasSymptom.getPositionalInfo().length());
//            cHsympt++;
//        }
//        logger.info("END batch has symptom insert");



    }



    public int countHasSemTypes(List<Symptom> symptoms){
        int count = 0;
        for (Symptom symptom: symptoms) {
            count += symptom.getSemanticTypes().size();
        }
        return count;
    }


    public int countHasSymptom(List<edu.ctb.upm.midas.model.filter.metamap.response.Text> textList){
        int count = 1;//1 para ajustar
        for (edu.ctb.upm.midas.model.filter.metamap.response.Text text: textList) {
//            List<Concept> noRepeatedConcepts = removeRepetedConcepts(text.getConcepts());
            count += removeRepetedConcepts(text.getConcepts()).size();
        }
        return count;
    }


    public List<Concept> removeRepetedConcepts(List<Concept> elements){
        //Se crea esta lista para no afectar a la original
        List<Concept> elements_2 = new ArrayList<>();
        elements_2.addAll(elements);
        List<Concept> resList = elements_2;
        Set<Concept> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements_2);
        elements_2.clear();
        elements_2.addAll(linkedHashSet);

        return resList;
    }


    public List<Symptom> removeRepetedSymptoms(List<Symptom> elements){
        List<Symptom> resList = elements;
        Set<Symptom> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements);
        elements.clear();
        elements.addAll(linkedHashSet);

        return resList;
    }

    public List<edu.ctb.upm.midas.model.jpa.Symptom> removeRepetedSymptomsJpa(List<edu.ctb.upm.midas.model.jpa.Symptom> elements){
        List<edu.ctb.upm.midas.model.jpa.Symptom> resList = elements;
        Set<edu.ctb.upm.midas.model.jpa.Symptom> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements);
        elements.clear();
        elements.addAll(linkedHashSet);

        return resList;
    }

    public List<SemanticType> removeRepetedSemanticTypes(List<SemanticType> elements){
        List<SemanticType> resList = elements;
        Set<SemanticType> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements);
        elements.clear();
        elements.addAll(linkedHashSet);

        return resList;
    }

    public List<edu.ctb.upm.midas.model.jpa.SemanticType> removeRepetedSemanticTypesJpa(List<edu.ctb.upm.midas.model.jpa.SemanticType> elements){
        List<edu.ctb.upm.midas.model.jpa.SemanticType> resList = elements;
        Set<edu.ctb.upm.midas.model.jpa.SemanticType> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements);
        elements.clear();
        elements.addAll(linkedHashSet);

        return resList;
    }


    //Para hacer inserta más rápidos noRepeatedConcepts
    public int createHasSymptom(List<Concept> concepts, List<Concept> noRepeatedConcepts, List<HasSymptom> hasSymptoms, String textId, int conceptCount, int countTotalHasSymptom, FileWriter fileWriter){
        //System.out.println("concepts: " + concepts.size() + " noRepetead: " + noRepeatedConcepts.size());
        try {
            int countC = 1;
            for (Concept uniqueConcept : noRepeatedConcepts) {
                conceptCount++;
                /*if (!isInvalidatedSemanticType(uniqueConcept.getSemanticTypes())) {*/
                    HasSymptom hasSymptom = new HasSymptom(textId, uniqueConcept.getCui(), (byte) 0);
                    //System.out.println("ConceptUnique: " + uniqueConcept.getCui());
                    final int[] count = {1};
                    concepts.stream().filter(o -> o.getCui().equals(uniqueConcept.getCui())).forEach(
                            o -> {
                                String matchedWords_ = "";
                                String positionalInfo_ = "";
                                if (count[0] == 1) {
                                    matchedWords_ = o.getMatchedWords().toString();
                                    positionalInfo_ = o.getPositionalInfo();
                                } else {
                                    matchedWords_ = hasSymptom.getMatchedWords() + "&" + o.getMatchedWords().toString();
                                    positionalInfo_ = hasSymptom.getPositionalInfo() + "&" + o.getPositionalInfo();
                                }
                                hasSymptom.setMatchedWords(matchedWords_);
                                hasSymptom.setPositionalInfo(positionalInfo_);

                                //System.out.println("    " + count + ". concept: " + o.getCui() + " = match: " + o.getMatchedWords().toString());

                                count[0]++;
                            }
                    );
                    //
                    hasSymptoms.add(hasSymptom);
                    fileWriter.write("('" + hasSymptom.getTextId() + "', '" + hasSymptom.getCui() + "', 0, \"" + hasSymptom.getMatchedWords() + "\", \"" + hasSymptom.getPositionalInfo() + "\")" + (conceptCount==countTotalHasSymptom?Constants.COMMA_DOT:Constants.COMMA+"\n"));
                    System.out.println(conceptCount + ". " + hasSymptom);

                /*}*/
            }
        }catch (Exception e){
            System.out.println("Mensaje de la excepción 2: " + e.getMessage());
        }

        return conceptCount;

    }


    public int createHasSymptomElement(List<Concept> concepts, List<Concept> noRepeatedConcepts, List<edu.ctb.upm.midas.model.jpa.HasSymptom> hasSymptoms, String textId, int conceptCount, int countTotalHasSymptom, List<String> hasSymptomInsertElements){
        //System.out.println("concepts: " + concepts.size() + " noRepetead: " + noRepeatedConcepts.size());
        try {
            int countC = 1;
            for (Concept uniqueConcept : noRepeatedConcepts) {
                conceptCount++;
                /*if (!isInvalidatedSemanticType(uniqueConcept.getSemanticTypes())) {*/
                edu.ctb.upm.midas.model.jpa.HasSymptom hasSymptom = new edu.ctb.upm.midas.model.jpa.HasSymptom(textId, uniqueConcept.getCui(), (byte) 0);
                //System.out.println("ConceptUnique: " + uniqueConcept.getCui());
                final int[] count = {1};
                concepts.stream().filter(o -> o.getCui().equals(uniqueConcept.getCui())).forEach(
                        o -> {
                            String matchedWords_ = "";
                            String positionalInfo_ = "";
                            if (count[0] == 1) {
                                matchedWords_ = o.getMatchedWords().toString();
                                positionalInfo_ = o.getPositionalInfo();
                            } else {
                                matchedWords_ = hasSymptom.getMatchedWords() + "&" + o.getMatchedWords().toString();
                                positionalInfo_ = hasSymptom.getPositionalInfo() + "&" + o.getPositionalInfo();
                            }
                            hasSymptom.setMatchedWords(matchedWords_);
                            hasSymptom.setPositionalInfo(positionalInfo_);

                            //System.out.println("    " + count + ". concept: " + o.getCui() + " = match: " + o.getMatchedWords().toString());

                            count[0]++;
                        }
                );
                //
                hasSymptoms.add(hasSymptom);
                String hasSymptomElement = "('" + hasSymptom.getTextId() + "', '" + hasSymptom.getCui() + "', 0, \"" + hasSymptom.getMatchedWords() + "\", \"" + hasSymptom.getPositionalInfo() + "\")";
                hasSymptomInsertElements.add(hasSymptomElement);
//                System.out.println(conceptCount + ". " + hasSymptom);

                /*}*/
            }
        }catch (Exception e){
            System.out.println("Mensaje de la excepción 2: " + e.getMessage());
        }

        return conceptCount;

    }


    public boolean isInvalidatedSemanticType(List<String> semtypes){
        boolean res = false;
        for (String sem: semtypes) {
            if (sem.equalsIgnoreCase("clna") || sem.equalsIgnoreCase("qlco") || sem.equalsIgnoreCase("hcpp"))res = true;break;
        }
        return res;
    }


    public boolean containsProcess(final List<Text> texts, String textId){
        return texts.stream()
                .filter(o -> o.getId().trim() != null)
                .filter(o -> o.getId().trim().contentEquals(textId.trim()))
                .findFirst()
                .isPresent();
    }


    public Response readMetamapResponseJSON(Consult consult, boolean onlyTexts) throws Exception {
        List<edu.ctb.upm.midas.model.filter.metamap.response.Text> texts = new ArrayList<>();
        Response response = null;
        System.out.println("Read JSON!...");
        Gson gson = new Gson();
        String fileName = consult.getSnapshot() + "_" + consult.getSource() + Constants.METAMAP_FILE_NAME + Constants.DOT_JSON;//adis = disease album
        String path = Constants.METAMAP_FOLDER + fileName;

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            if (onlyTexts) {
                ProcessedText resp = gson.fromJson(br, ProcessedText.class);
                texts = resp.getTexts();
            }else{
                response = gson.fromJson(br, Response.class);
                texts = response.getTextList();
            }
        }catch (Exception e){
            System.out.println("Error to read or convert JSON!...");
        }

        /*for (edu.upm.midas.data.validation.metamap.model.response.Text text: resp.retrieveTexts()) {
            System.out.println("TextId: " + text.getId() + " | Concepts: " + text.getConcepts().toString());
        }*/

        return response;
    }


    public void writeJSONFile(String diseaseJsonBody, Consult consult) throws IOException {
        String fileName = consult.getSnapshot() + "_" + consult.getSource() + Constants.METAMAP_FILE_NAME + Constants.DOT_JSON;
        String path = Constants.METAMAP_FOLDER + fileName;
        InputStream in = getClass().getResourceAsStream(path);
        //BufferedReader bL = new BufferedReader(new InputStreamReader(in));
        File file = new File(path);
        BufferedWriter bW;

        if (!file.exists()){
            bW = new BufferedWriter(new FileWriter(file));
            bW.write(diseaseJsonBody);
            bW.close();
        }
    }


    /**
     * @param consult
     */
    @Transactional
    public void filterDiseaseName(Consult consult){
        Request request = new Request();
        Configuration conf = new Configuration();
        List<Text> texts = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        conf.setOptions("-y -R");
        List<String> sources = new ArrayList<>();
        sources.add("SNOMEDCT_US");
        conf.setSources(sources);
        conf.setSemanticTypes(Constants.SEMANTIC_TYPES_LIST);

        request.setConfiguration( conf );

        List<Object[]> diseases = diseaseService.findAllBySourceAndVersionNative(consult.getSource(), consult.getDate());
        if (diseases != null) {
            for (Object[] disease : diseases) {
                Text text = new Text();
                text.setId((String) disease[0]);
                text.setText((String) disease[1]);
                texts.add(text);
                System.out.println((String) disease[1]);
            }
        }
        request.setTextList(texts);

        System.out.println( "Connection_ with METAMAP API..." );
        System.out.println( "Founding medical concepts in a texts... please wait, this process can take from minutes to hours... " );
        Response response = metamapResourceService.filterDiseaseName( request );

        System.out.println( gson.toJson( response ) );
        //response.getConfiguration().toString();
        int count = 1;
        for (edu.ctb.upm.midas.model.filter.metamap.response.Text text:
             response.getTextList()) {
            System.out.println(String.format("%06d", count));
            System.out.println("TEXT_ID: " + text.getId() + " | CONCEPTS("+text.getConcepts().size()+"): " + text.getConcepts());
            count++;
        }

        String json = gson.toJson( response.getConfiguration() );
        //System.out.println(json);
        System.out.println(utilDate.getTimestampNumber());
        System.out.println(utilDate.getTime());

    }



}
