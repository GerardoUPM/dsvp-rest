package edu.ctb.upm.midas.service._extract;

import edu.ctb.upm.midas.client_modules.extraction.mayoclinic.texts_extraction.api_response.MayoClinicTextsExtractionService;
import edu.ctb.upm.midas.client_modules.extraction.wikipedia.disease_list.api_response.DiseaseAlbumResourceService;
import edu.ctb.upm.midas.common.util.Common;
import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.constants.Constants;
import edu.ctb.upm.midas.enums.StatusHttpEnum;
import edu.ctb.upm.midas.model.WebLink;
import edu.ctb.upm.midas.model.common.document_structure.*;
import edu.ctb.upm.midas.model.common.document_structure.text.List_;
import edu.ctb.upm.midas.model.common.document_structure.text.Paragraph;
import edu.ctb.upm.midas.model.common.document_structure.text.Text;
import edu.ctb.upm.midas.model.extraction.common.request.Request;
import edu.ctb.upm.midas.model.extraction.common.request.RequestJSON;
import edu.ctb.upm.midas.model.extraction.common.response.Response;
import edu.ctb.upm.midas.model.jpa.Document;
import edu.ctb.upm.midas.model.jpa.DocumentUrl;
import edu.ctb.upm.midas.model.jpa.HasDisease;
import edu.ctb.upm.midas.model.jpa.Url;
import edu.ctb.upm.midas.scheduling.TvpScheduling;
import edu.ctb.upm.midas.service._populate.MayoClinicPopulateDbNative;
import edu.ctb.upm.midas.service.jpa.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 29/01/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className ExtractService
 * @see
 */
@Service
public class MayoClinicExtractService {

    private static final Logger LOG = LoggerFactory.getLogger(MayoClinicExtractService.class);

    @Autowired
    private MayoClinicPopulateDbNative mayoclinicPopulateDbNative;
    @Autowired
    private DiseaseAlbumResourceService diseaseAlbumResource;
    @Autowired
    private MayoClinicTextsExtractionService mcteService;
    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private Constants constants;
    @Autowired
    private Common common;

    @Autowired
    private DocumentService documentService;


    /**
     * @return
     * @throws Exception
     */
    public boolean extract(String snapshot, boolean json, boolean fix) throws Exception {
        boolean res = false;
        String inicio = timeProvider.getTime();
        snapshot = (json)?snapshot:timeProvider.sqlDateFormatyyyyMMdd(timeProvider.getSqlDate());
//        Date snapshot = timeProvider.getSqlDate();
//        Response response = retrieveTexts(json, timeProvider.dateFormatyyyyMMdd(snapshot));
        Response response = retrieveTexts(json, snapshot);

        if (response!=null) {
            if (response.getResponseCode().equals(StatusHttpEnum.OK.getClave())) {
                if (response.getSources() != null) {
//                    mayoclinicPopulateDbNative.populate(response.getSources(), snapshot, json);
                    mayoclinicPopulateDbNative.populate(response.getSources(), timeProvider.stringToDate(snapshot), json, fix);
                } else {
                    //Source vacío
                }
            } else {
                //Estatus error
            }
        }else{
            LOG.error("Response null");
        }
        LOG.info("Inicio:" + inicio + " | Termino: " + timeProvider.getTime());

        return res;
    }

    /**
     * @param isJSONRequest
     * @param snapshot
     * @return
     */
    public Response retrieveTexts(boolean isJSONRequest, String snapshot){
        System.out.println("Start Connection with MAYOCLINIC TEXT EXTRACTION API REST...");
        System.out.println("Get all texts from MayoClinic disease articles... please wait, this process can take from minutes or hours... ");

        Response response;
        if (isJSONRequest){
            RequestJSON request = new RequestJSON();
            request.setSnapshot(snapshot);
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            System.out.println(gson.toJson(mcteService.getTextsByJSON(request)));
            response = mcteService.getTextsByJSON(request);
        }else {
            Request request = new Request();
            request.setSnapshot(snapshot);
            request.setExtractionType("");
            //Opción para guardar JSON y consumirlo después (queue)
            request.setJson(true);
            response = mcteService.getTexts(request);
        }
        System.out.println("End Connection with MAYOCLINIC TEXT EXTRACTION API REST... startTime: " + response.getStart_time() + " endTime: "+ response.getEnd_time());
        System.out.println("getResponseCode: " + response.getResponseCode());

        return response;
    }

    /**
     * @param snapshot
     * @return
     * @throws Exception
     */
    public boolean printReport(String snapshot, boolean isJSONRequest) throws Exception {
        boolean res = false;
        String inicio = timeProvider.getTime();
        Date version = timeProvider.getSqlDate();
        //populateDbNative.onlyExtract();
        Response response = retrieveTexts(isJSONRequest, snapshot);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(gson.toJson(response));
//        System.out.println(response);
        if (response!=null) printReport(response.getSources(), isJSONRequest);
        System.out.println("Inicio:" + inicio + " | Termino: " + timeProvider.getTime());

        return res;
    }

    /**
     * Método que muestra un reporte de la extracción recien hecha
     *
     * @throws Exception
     */
    public void printReport(List<Source> sourceList, boolean isJSONRequest) throws Exception {

        List<Integer> countCharacteresList = new ArrayList<>();
        long time_start, time_end;

        boolean hasCodes = false;
        boolean hasSections = false;

        int countValid = 1;

        System.out.println("-------------------- EXTRACTION REPORT --------------------");
        for (Source source : sourceList) {
            System.out.println("\n");
            System.out.println("-------------------- SOURCE(" + source.getId() + "_" + source.getName() + ") --------------------");
            for (Doc document: source.getDocuments()) {
                System.out.println("Doc(" + document.getId() + "_" + document.getDate() + ") => " + document.getUrl().getUrl());
                System.out.println("    Disease(" + document.getDisease().getId() + "_" + document.getDisease().getName() + ") ");
                if (document.getDisease().getSynonyms()!=null) {
                    for (Synonym synonym :document.getDisease().getSynonyms()) {
                        System.out.println("        Synonym(" + synonym.getName() + ")");
                    }
                }

                if (document.getSectionList()!=null) {
                    for (Section section : document.getSectionList()) {
                        System.out.println("    Section(" + section.getId() + ") " + section.getName());

                        for (Text text : section.getTextList()) {
                            if (isJSONRequest) text = common.getTypeText(text);

                            System.out.println("    ------------ TEXT(" + text.getTitle() + ") -----------");
                            System.out.println("        Text_" + text.getTextOrder() + "(" + text.getId() + ") (" + text.getClass() + ")");

                            String aux = "";
                            if (text instanceof Paragraph) {
                                System.out.println("            " + ((Paragraph) text).getText());
                                countCharacteresList.add(((Paragraph) text).getText().length());
                            } else if (text instanceof List_) {
                                for (String bullet : ((List_) text).getBulletList()) {
                                    System.out.println("            -" + bullet);
                                    aux = aux + bullet + "&";
                                }
                                //if(aux.length() > 2){aux = aux.substring(0, aux.length()-1);}
                                //System.out.println(" aux = " + aux);
                                countCharacteresList.add(aux.length());
                            }

                            if (text.getUrlList() != null) {
                                System.out.println("        ------------ LINKS -----------");
                                for (Link url : text.getUrlList()) {
                                    //System.out.println("            Key: " + url.getId() + ": URL(" + url.getDescription() + "): " + url.getUrl() );
                                }
                            }

                        }
                    }
                    if (document.getSectionList().size() > 0) hasSections = true;
                }

                if (hasCodes || hasSections) {
                    document.setDiseaseArticle(true);
                    countValid++;
                    hasCodes = false;
                    hasSections = false;
                }
                System.out.println("Doc(|" + document.getId() + " | " + document.getDate() + " | " + document.isDiseaseArticle() + " | " + document.getUrl().getUrl());

            }

            System.out.println("# de Documentos " + source.getDocuments().size());
            System.out.println("# de Documentos válidos " + countValid);
            System.out.println("# de Documentos no válidos " + (source.getDocuments().size() - countValid) );

        }



/*
        System.out.println("=========================================================");
        Collections.sort(countCharacteresList);
        for (Integer i:
             countCharacteresList) {
            System.out.println(i);
        }
*/



        //System.out.println("the task (model informtacion of diseases from wikipedia) has taken "+ ( (time_end - time_start) / 1000 ) +" seconds");

    }

    public void printFromDatabase(){
        List<WebLink> xmlLinks = new ArrayList<>();
        List<Document> documents = documentService.findAll();
        System.out.println("size: "+documents.size());
        int count = 1;
        for (Document document: documents) {
            if (document.getDate().toString().equals("2018-02-15")){
                List<HasDisease> hasDiseases = document.getHasDiseases();
                String diseaseName = "";
                for (HasDisease hasDisease:hasDiseases){
                    edu.ctb.upm.midas.model.jpa.Disease disease = hasDisease.getDiseaseByDiseaseId();
                    diseaseName = diseaseName + disease.getName() + "; ";
                }
                List<DocumentUrl> documentUrls = document.getDocumentUrls();
                String urls = "";
                WebLink xmlLink = new WebLink();
                for (DocumentUrl documentUrl: documentUrls) {
                    String urlId = documentUrl.getUrlId();
                    Url url = documentUrl.getUrlByUrlId();
                    urls = urls + url.getUrl() + "; ";

                    xmlLink.setUrl(url.getUrl());
                    xmlLink.setConsult(diseaseName);
                    break;
                }
                System.out.println("Disease: (" + count +")" + xmlLink.getConsult() + " | URL: " + xmlLink.getUrl());
                xmlLinks.add(xmlLink);
                count++;
            }
        }
        //extractService.onlyExtract(xmlLinks);
    }
}
