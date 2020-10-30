package edu.ctb.upm.midas.service.filter.tvp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ctb.upm.midas.client_modules.filter.tvp.api_response.impl.TvpResourceServiceImpl;
import edu.ctb.upm.midas.constants.Constants;
import edu.ctb.upm.midas.model.filter.common.Consult;
import edu.ctb.upm.midas.service.filter.helper.ConsultHelper;
import edu.ctb.upm.midas.model.filter.common.query.ResponseSymptom;
import edu.ctb.upm.midas.model.filter.tvp.TvpConfiguration;
import edu.ctb.upm.midas.model.filter.tvp.request.Request;
import edu.ctb.upm.midas.model.filter.tvp.response.Concept;
import edu.ctb.upm.midas.model.filter.tvp.response.MatchNLP;
import edu.ctb.upm.midas.model.filter.tvp.response.Response;
import edu.ctb.upm.midas.service.jpa.HasSymptomService;
import edu.ctb.upm.midas.service.jpa.SourceService;
import edu.ctb.upm.midas.service.jpa.helperNative.ConfigurationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gerardo on 24/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className TvpService
 * @see
 */
@Service
public class TvpService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SourceService sourceService;
    @Autowired
    private HasSymptomService hasSymptomService;
    @Autowired
    private ConsultHelper consultHelper;
    @Autowired
    private TvpResourceServiceImpl tvpResource;
    @Autowired
    private ConfigurationHelper configurationHelper;
    @Autowired
    private Constants constants;


    /**
     * @param consult
     * @throws Exception
     */
    public void filter(Consult consult, boolean storage) throws Exception {

        String fileName = consult.getSnapshot() + "_updates_has_symptom.txt";
        String path = Constants.TVP_RETRIEVAL_HISTORY_FOLDER + fileName;
//        FileWriter fileWriter = new FileWriter(path);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TvpConfiguration tvpConfiguration = new TvpConfiguration();
        //Colocar una validación para Consult...
        String sourceId = sourceService.findByNameNative( consult.getSource() );
        System.out.println( "Source: " + sourceId);
        System.out.println( "Read symptoms to the DB..." );
        List<ResponseSymptom> responseSymptoms = consultHelper.findSymptomsByVersionAndSource( consult );
        tvpConfiguration.setTermsFound(responseSymptoms.size());
        System.out.println( "Symptoms found: " + tvpConfiguration.getTermsFound() );
        System.out.println( "Removing repeated symptoms..." );
        List<Concept> nonRepetedSymptoms = getConceptList(responseSymptoms );
        tvpConfiguration.setNonRepetedTerms(nonRepetedSymptoms.size());
        System.out.println( "NonRepetedSymptoms: " + nonRepetedSymptoms.size() );
        System.out.println( "Creating request..." );
        Request request = new Request();
        request.setConcepts( nonRepetedSymptoms );
        request.setSource(consult.getSource());
        request.setSnapshot(consult.getSnapshot());
        request.setToken( "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJncmFyZG9sYWdhckBob3RtYWlsLmNvbSIsImF1ZCI6IndlYiIsIm5hbWUiOiJHZXJhcmRvIExhZ3VuZXMiLCJ1c2VyIjp0cnVlLCJpYXQiOjE1MDk2MTQyNjh9.uVhDgfLrAgdnj02Hsbgfj9tkVlfni89i0hKVYW31eHApCHpheikK9ae1MhbzRhiyUcFGMKwtiyVgff5NCMY3PA" );
        //printConcepstJSON( nonRepetedSymptoms );
        System.out.println(request);
        System.out.println( "Connection_ with TVP API..." );
        System.out.println( "Validating symptoms... please wait, this process can take from minutes to hours... " );

        Response response;

        //CONSUMIR UN JSON
        if (consult.isJson()){
            response = readTVPValidationJSON(consult);
        }else{
            response = tvpResource.getValidateSymptoms( request );
        }
        System.out.println("Authorization: "+ response.isAuthorized());

        if (storage) {
            if (response.isAuthorized()) {
                int validatedSymptoms = 0;
                /* Actualizar entidad HasSymptom con CUI y textId */
                System.out.println("Authorization: " + response.getValidatedConcepts().size() + "|" + responseSymptoms.size());
                for (MatchNLP matchNLP : response.getValidatedConcepts()) {//ResponseSymptom
                    //MatchNLP matchNLP = exist(symptom.getCui(), response.getValidatedConcepts());//antes matchNLPList
                    if (matchNLP.hasMatches()) {
                        System.out.println(validatedSymptoms + " to " + response.getValidatedConcepts().size() + " Symptom validated! | " + matchNLP.getConcept().getCui() + "==" + matchNLP.getConcept().toString());
                        hasSymptomService.updateValidatedNative(consult.getSnapshot(), sourceId, matchNLP.getConcept().getCui(), true);
//                    fileWriter.write("UPDATE has_symptom h " +
//                            "SET h.validated = 1 " +
//                            "WHERE h.text_id LIKE '%"+consult.getSnapshot()+"%' " +
//                            "AND h.text_id LIKE '%"+sourceId+"%' " +
//                            "AND h.cui = '"+matchNLP.getConcept().getCui()+"';\n");
                        validatedSymptoms++;
                        System.out.println("Update symptom in DB ready!");
                    } else {
                        System.out.println("Symptom not found:" + matchNLP.getConcept().getCui());
                    }
                }
//            fileWriter.close();
                System.out.println("Start insert configuration...");
                tvpConfiguration.setValidatedNonRepetedTerms(validatedSymptoms);
                String configurationJson = gson.toJson(tvpConfiguration);
                configurationHelper.insert(consult.getSource(), consult.getDate(), constants.SERVICE_TVP_CODE, configurationJson);
                System.out.println("End insert configuration ready!...");
            } else {
                System.out.println("Authorization message: " + response.getAuthorizationMessage() + " | token: " + response.getToken());
            }
        }else{//NO SE INSERTARÁN DATOS
            System.out.println("Datas no storage!...");
            System.out.println("End Term Validation Procedure!...");
        }

    }


    /**
     * @param responseSymptoms
     * @return
     */
    public List<Concept> getConceptList(List<ResponseSymptom> responseSymptoms){
        List<Concept> concepts = new ArrayList<Concept>();
        for (ResponseSymptom symptom: responseSymptoms) {
            Concept concept = new Concept();
            concept.setCui( symptom.getCui() );
            concept.setName( symptom.getSymptomName() );
            concepts.add( concept );
        }
        return removeRepetedConcepts( concepts );
    }


    /**
     * @param elements
     * @return
     */
    public List<Concept> removeRepetedConcepts(List<Concept> elements){
        List<Concept> resList = elements;
        Set<Concept> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements);
        elements.clear();
        elements.addAll(linkedHashSet);
        return resList;
    }


    /**
     * @param cui
     * @param matchNLPList
     * @return
     */
    public MatchNLP exist(String cui, List<MatchNLP> matchNLPList){
        for (MatchNLP matchNLP: matchNLPList) {
            if (matchNLP.getConcept().getCui().equals( cui )){
                return matchNLP;
            }
        }
        return null;
    }


    /**
     * @param concepts
     * @throws JsonProcessingException
     */
    public void printConcepstJSON(List<Concept> concepts) throws JsonProcessingException {
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(concepts));
    }


    /**
     * @param consult
     * @return
     * @throws Exception
     */
    public Response readTVPValidationJSON(Consult consult) throws Exception {
        Response response = null;
        Gson gson = new Gson();
        String fileName = consult.getSnapshot() + Constants.UNDER_SCORE + consult.getSource() + Constants.TVP_RETRIEVAL_FILE_NAME + Constants.DOT_JSON;
        String path = Constants.TVP_RETRIEVAL_HISTORY_FOLDER + fileName;
        System.out.println("Read JSON!..." + path);

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            response = gson.fromJson(br, Response.class);
        }catch (Exception e){
            System.out.println("Error to read or convert JSON!...");
        }

        return response;
    }

}
