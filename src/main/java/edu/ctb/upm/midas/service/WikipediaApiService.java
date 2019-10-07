package edu.ctb.upm.midas.service;

import com.google.gson.*;
import edu.ctb.upm.midas.common.util.Common;
import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.constants.Constants;
import edu.ctb.upm.midas.model.common.document_structure.Link;
import edu.ctb.upm.midas.model.common.document_structure.Reference;
import edu.ctb.upm.midas.model.wikipediaApi.*;
import edu.ctb.upm.midas.service.jpa.DocumentService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WikipediaApiService {

    private static final Logger logger = LoggerFactory.getLogger(WikipediaApiService.class);

    private String encoding = "UTF-8";
    @Autowired
    private DocumentService documentService;

    public void init(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Common common = new Common();
        TimeProvider timeProvider = new TimeProvider();
//        List<String> diseaseListError = findErrorsInTheLog();

        List<Disease> diseases = documentService.findAllDistinctArticlesAndSnapshot();
        int count = 1, total = diseases.size();
        if (diseases.size()>0) {
            for (Disease disease : diseases) {
                logger.info(count + ". DISEASE to " + total + " (" + (count*100)/total + "%)." + disease.getName() /*+ " | " + disease.getSnapshotId() + " | " + disease.getCurrentSnapshot() + " | " + disease.getPreviousSnapshot()*/);
//                if (disease.getName().trim())
                List<Snapshot> snapshots = documentService.findAllSnapshotsOfAArticle(disease.getId());
                if (snapshots!=null) {
                    Page page = getPageIdAndTheirSpecificRevisionByTitleAndSnapshot(disease, snapshots);
                    disease.setPage(page);
                    disease.setSnapshots(snapshots);
                    //Escribir json
                    try {
                        String fileNAme = common.writeAnalysisJSONFile(gson.toJson(disease), disease, count, timeProvider.getNowFormatyyyyMMdd(), Constants.STATISTICS_HISTORY_FOLDER);
                        logger.info("Write JSON file successful! => " + fileNAme);
                    }catch (Exception e){logger.error("Error to write the JSON file", e);}
                }
//                if (count==3) break;
                count++;
            }
            logger.info("End procedure");
        }
//        System.out.println(diseases.toString());
    }


    public void initCompleteSnapshots(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Common common = new Common();
        TimeProvider timeProvider = new TimeProvider();
//        List<String> diseaseListError = findErrorsInTheLog();

        List<Snapshot> commonSnapshots = new ArrayList<Snapshot>() {{
            add(new Snapshot(1, "2018-02-01", ""));
            add(new Snapshot(2, "2018-02-15", ""));
            add(new Snapshot(3, "2018-03-01", ""));
            add(new Snapshot(4, "2018-03-15", ""));
            add(new Snapshot(5, "2018-04-01", ""));
            add(new Snapshot(6, "2018-04-15", ""));
            add(new Snapshot(7, "2018-05-01", ""));
            add(new Snapshot(8, "2018-05-15", ""));
            add(new Snapshot(9, "2018-06-01", ""));
            add(new Snapshot(10, "2018-06-15", ""));
            add(new Snapshot(11, "2018-07-01", ""));
            add(new Snapshot(12, "2018-07-15", ""));
            add(new Snapshot(13, "2018-08-01", ""));
            add(new Snapshot(14, "2018-08-15", ""));
            add(new Snapshot(15, "2018-09-01", ""));
            add(new Snapshot(16, "2018-09-15", ""));
            add(new Snapshot(17, "2018-10-01", ""));
            add(new Snapshot(18, "2018-10-15", ""));
            add(new Snapshot(19, "2018-11-01", ""));
            add(new Snapshot(20, "2018-11-15", ""));
            add(new Snapshot(21, "2018-12-01", ""));
            add(new Snapshot(22, "2018-12-15", ""));
            add(new Snapshot(23, "2019-01-01", ""));
            add(new Snapshot(24, "2019-01-15", ""));
            add(new Snapshot(25, "2019-02-01", ""));
            add(new Snapshot(26, "2019-02-15", ""));
            add(new Snapshot(27, "2019-03-01", ""));
            add(new Snapshot(28, "2019-03-15", ""));
            add(new Snapshot(29, "2019-04-01", ""));
            add(new Snapshot(30, "2019-04-15", ""));
            add(new Snapshot(31, "2019-05-01", ""));
            add(new Snapshot(32, "2019-05-15", ""));
            add(new Snapshot(33, "2019-06-01", ""));
            add(new Snapshot(34, "2019-06-15", ""));
            add(new Snapshot(35, "2019-07-01", ""));
            add(new Snapshot(36, "2019-07-15", ""));
            add(new Snapshot(37, "2019-08-01", ""));
            add(new Snapshot(38, "2019-08-15", ""));
        }};

        List<Disease> diseases = documentService.findAllDistinctArticlesAndSnapshot();
        int count = 1, total = diseases.size();
        if (diseases.size()>0) {
            for (Disease disease : diseases) {
                logger.info(count + ". DISEASE to " + total + " (" + (count*100)/total + "%)." + disease.getName() /*+ " | " + disease.getSnapshotId() + " | " + disease.getCurrentSnapshot() + " | " + disease.getPreviousSnapshot()*/);
                if (count>=3715) {
//                if (disease.getName().trim())
                    if (commonSnapshots != null) {
                        Page page = getPageIdAndTheirSpecificRevisionByTitleAndSnapshot(disease, commonSnapshots);
                        disease.setPage(page);
                        disease.setSnapshots(commonSnapshots);
                        disease.setSnapshotCount(commonSnapshots.size());
                        //Escribir json
                        try {
                            String fileNAme = common.writeAnalysisJSONFile(gson.toJson(disease), disease, count, timeProvider.getNowFormatyyyyMMdd(), Constants.ANALYSIS_2_HISTORY_DIRECTORY);
                            logger.info("Write JSON file successful! => " + fileNAme);
                        } catch (Exception e) {
                            logger.error("Error to write the JSON file", e);
                        }
                        disease.getPage().setRevisions(null);
                        disease.setPage(null);
                        disease.setSnapshots(null);
                        disease = null;
                    }
                }
//                if (count==3) break;
                count++;
            }
            logger.info("End procedure");
        }
//        System.out.println(diseases.toString());
    }


    public void staticTest(){

//        List<Snapshot> snapshots = new ArrayList<Snapshot>(){{
//            add(new Snapshot(1, "2018-02-01", "2018-02-01"));
//            add(new Snapshot(2, "2018-02-15", "2018-02-15"));
//        }};
        List<Disease> diseases = new ArrayList<Disease>(){{
            add(new Disease("DIS002645", "Pediatric acute-onset neuropsychiatric syndrome (PANS)"));
            add(new Disease("DIS003200", "Alpha 1-antitrypsin deficiency"));
            add(new Disease("DIS003545", "Diabetes mellitus type 1"));
            add(new Disease("DIS004064", "Diabetes mellitus"));
            add(new Disease("DIS004098", "Erythema chronicum migrans"));
            add(new Disease("DIS004879", "Chlamydia infection"));
            add(new Disease("DIS005124", "Diabetes mellitus type 2"));
            add(new Disease("DIS005383", "Poliomyelitis"));
            add(new Disease("DIS006298", "Paraproteinemia"));
            add(new Disease("DIS006481", "Cri du chat"));
            add(new Disease("DIS006526", "49,XXXXX"));
            add(new Disease("DIS006848", "Thrombocytosis"));
            add(new Disease("DIS007260", "Idiopathic CD4+ lymphocytopenia"));
            add(new Disease("DIS007413", "Cornelia de Lange Syndrome"));
            add(new Disease("DIS007485", "Essential thrombocytosis"));
            add(new Disease("DIS007956", "Pertussis"));
            add(new Disease("DIS010801", "Neonatal diabetes mellitus"));
        }};
        Common common = new Common();
        TimeProvider timeProvider = new TimeProvider();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Disease disease = new Disease("DIS009247", "Stiff-Person syndrome");
        for (Disease disease: diseases) {
            List<Snapshot> snapshots = documentService.findAllSnapshotsOfAArticle(disease.getId());
            if (snapshots != null) {
                Page page = getPageIdAndTheirSpecificRevisionByTitleAndSnapshot(disease, snapshots);
                disease.setPage(page);
                disease.setSnapshots(snapshots);
//            System.out.println(page);
                //Escribir json
                try {
                    String fileNAme = common.writeAnalysisJSONFile(gson.toJson(disease), disease, 1, timeProvider.getNowFormatyyyyMMdd(), "tmp/last_diseases/");
                    logger.info("Write JSON file successful! => " + fileNAme);
                } catch (Exception e) {
                    logger.error("Error to write the JSON file", e);
                }
            }
        }
//        Page page = getPageIdAndTheirSpecificRevisionByTitleAndSnapshot("COACH syndrome", snapshots);
//        System.out.println(page);

    }


    public void test(){
        System.out.println("test method");

        Common common = new Common();
        TimeProvider timeProvider = new TimeProvider();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<Integer, Disease> diseaseMap = findErrorsInTheLog();
        for (Integer key: diseaseMap.keySet()) {
            Disease disease = diseaseMap.get(key);
//            System.out.println("Clave: " + key + " -> Valor: " + disease);


            logger.info(key + ". DISEASE to total? " + disease.getId() + ": " + disease.getName());
            List<Snapshot> snapshots = documentService.findAllSnapshotsOfAArticle(disease.getId());
            if (snapshots!=null) {
                Page page = getPageIdAndTheirSpecificRevisionByTitleAndSnapshot(disease, snapshots);
                disease.setPage(page);
                disease.setSnapshots(snapshots);
                //Escribir json
                try {
                    String fileNAme = common.writeAnalysisJSONFile(gson.toJson(disease), disease, key, timeProvider.getNowFormatyyyyMMdd(), Constants.ANALYSIS_HISTORY_DIRECTORY);
                    logger.info("Write JSON file successful! => " + fileNAme);
                    page=null;
                    disease=null;
                }catch (Exception e){logger.error("Error to write the JSON file", e);}
            }



        }


    }


    public Page getPageIdAndTheirSpecificRevisionByTitleAndSnapshot(Disease disease, List<Snapshot> snapshots){
        Page page = new Page();
        Revision previousR = null;
        List<Revision> revisionList = new ArrayList<>();
        try {
            int snapshotCount = 1;
            for (Snapshot snapshot: snapshots) {
                try {
                    String responseWikipediaAPI = getWikipediaApiQueryResponse(disease.getName(), snapshot.getSnapshot());
                    Revision revision = null;
//            System.out.println("Wikipedia API response = " + responseWikipediaAPI);

                    //Parser string response Wikipedia API to Java JSON object
                    JsonElement jsonElement = parseWikipediaResponse(responseWikipediaAPI);
                    //Get information from Json object
                    JsonElement pages = jsonElement.getAsJsonObject().get("query").getAsJsonObject().get("pages");
                    //Obtiene todos los elementos de un JsonElement en forma de mapa
                    Set<Map.Entry<String, JsonElement>> elementPages = pages.getAsJsonObject().entrySet();

                    //Valida que el mapa no sea nulo
                    if (elementPages != null) {
                        //Recorre los elementos del mapa que corresponde al elemento con el pageid,
                        // => " {"24811533": "
                        for (Map.Entry<String, JsonElement> elementPage : elementPages) {
//                System.out.println( elementPage.getKey()+ " <-> " + elementPage.getValue());
                            //Obtiene todos los elementos de un JsonElement en forma de mapa
                            Set<Map.Entry<String, JsonElement>> elementPageInfo = elementPage.getValue().getAsJsonObject().entrySet();
                            //Valida que el mapa del elemento pages no sea nulo
                            if (elementPageInfo != null) {
                                //Recorre los elementos del mapa
                                for (Map.Entry<String, JsonElement> element : elementPageInfo) {
//                                System.out.println( element.getKey()+ " <-> " + element.getValue());

                                    //Verifica cada elemento para asignar sus valores a los campos correspondientes
                                    //del recien creado objeto Page
                                    getPageIdAndSetInPageObject(page, element);
                                    getPageTitleAndSetInPageObject(page, element);
//                                System.out.println(element.getKey() + " - " + element.getValue());
                                    //Valida que el elemento del mapa sea de nombre "revisions". Porque necesita
                                    //ser tratado especialmente para poder acceder a sus elementos y valores
                                    if (element.getKey().equalsIgnoreCase(Constants.REVISIONS_ELEMENT_NAME)) {
                                        //Parsea el elemento revision
                                        JsonElement revisionsSet = parseWikipediaResponse(
                                                //Para dar formato y hacer el parse a Json del String, es necesario
                                                //quitar el primer y el último elemento que son "[" y "]" para obtener un
                                                //String(JSON) que inicie y termine con "{" y "}"
                                                deleteFirstAndLastChar(element.getValue().toString())
                                        );
                                        //Doble verificación para saber si el elemento "revisions" es un objeto Json
                                        boolean isJsonObject = revisionsSet.isJsonObject();
                                        if (isJsonObject) {
                                            revision = new Revision();
                                            //Se recorren los elementos del mapa "revisions"
                                            for (Map.Entry<String, JsonElement> revElement : revisionsSet.getAsJsonObject().entrySet()) {
                                                getRevIdAndSetInRevisionObject(revision, revElement);
                                                getParentIdAndSetInRevisionObject(revision, revElement);
                                                getMinorAndSetInRevisionObject(revision, revElement);
                                                getUserAndSetInRevisionObject(revision, revElement);
                                                getUserIdAndSetInRevisionObject(revision, revElement);
                                                getTimestampAndSetInRevisionObject(revision, revElement);
                                                getSizeAndSetInRevisionObject(revision, revElement);
                                                getCommentAndSetInRevisionObject(revision, revElement);

//                                        System.out.println(revElement.getKey() + " - " + revElement.getValue());
                                            }
                                            if (previousR == null) {
                                                revision.setPreviousDate("");
                                            } else {
                                                revision.setPreviousDate(previousR.getDate());
                                            }
                                            snapshot.setRevId(revision.getRevid());
                                            revisionList.add(revision);
                                        }//END if (isJsonObject)
                                    }//END if compare if the element is kind of "revisions"
                                    if (element.getKey().equalsIgnoreCase(Constants.REDIRECTS_ELEMENT_NAME)) {
                                        JsonArray redirectSet = element.getValue().getAsJsonArray();
                                        //Doble verificación para saber si el elemento "revisions" es un objeto Json
                                        if (redirectSet != null) {
                                            for (JsonElement redirectElement : redirectSet) {
                                                JsonObject redirectObj = redirectElement.getAsJsonObject();
                                                Integer redirectpageid = (redirectObj.get(Constants.PAGES_ELEMENT_PAGEID_NAME) instanceof JsonNull) ? 0 : (redirectObj.get(Constants.PAGES_ELEMENT_PAGEID_NAME).getAsInt());
                                                String redirectpagetitle = redirectObj.get(Constants.PAGES_ELEMENT_TITLE_NAME).getAsString();
                                                if (disease.getName().equalsIgnoreCase(redirectpagetitle)) {
                                                    page.setIsredirect(true);
                                                    page.setRedirectpageid(redirectpageid);
                                                    page.setRedirectpagetitle(redirectpagetitle);
//                                                    break;
                                                }
                                            }
                                        }
                                    }//END if compare if the element is kind of "redirects"
                                }//END for that each element of pages element
                            }//END if (elementPageInfo!=null)
                        }
                    }//END if (elementPages!=null)

                    //Si las fechas son iguales significa que se trata de la misma actualización (revision) y por lo tanto
                    //el mismo texto de la anterior se debe colocar en la actual actualización (revision)
                    //con el fin de no hacer llamadas de más a la "Wikipedia API"
                    try {
                        if (revision.getDate().equalsIgnoreCase(revision.getPreviousDate())) {
                            //Si es la misma versión se copia la información del texto y de las secciones de la
                            //actualización (revision) anterior
//                    System.out.println("ES LA MISMA REVISIÓN: (" + revision.getDate() + "==" + revision.getPreviousDate() + ") => (" + revision.getSnapshot() + ")");
                            revision.setText(previousR.getText());
                            revision.setSectionCount(previousR.getSectionCount());
                            revision.setSections(previousR.getSections());
                            revision.setCharacterCount(previousR.getCharacterCount());
                        } else {
//                    System.out.println("NO ES LA MISMA REVISIÓN: (" + revision.getDate() + "==" + revision.getPreviousDate() + ") => (" + revision.getSnapshot() + ")");
                            try {
                                getRevisionTextAndSectionList(revision);
                            }catch (Exception e){
                                disease.setScorn(true);
                                logger.error("Error getPageIdAndTheirSpecificRevisionByTitleAndSnapshot in getRevisionTextAndSectionList: pageTitle:" + disease.getName() + " | snapshot:" + snapshot + " => REV:" + revision, e);
                            }
                        }
                    }catch (Exception e){
                        snapshot.setRevId(0);
                        disease.setScorn(true);
                        logger.error("Error getPageIdAndTheirSpecificRevisionByTitleAndSnapshot: pageTitle:" + disease.getName() + " | snapshot:" + snapshot + " => REV:" + revision, e);

                    }

//                System.out.println(revision.toString());

                    snapshotCount++;
                    revision.setSnapshot(snapshot.getSnapshot());
                    if (revision!=null) previousR = revision;
                }catch (Exception e){
                    disease.setScorn(true);
                    logger.error("Error getPageIdAndTheirSpecificRevisionByTitleAndSnapshot: pageTitle:" + disease.getName() + " | snapshot:" + snapshot, e);
                }
            }
            removeRepetedRevision(revisionList);
            if (page!=null){
                page.setRevisions(revisionList);
                page.setRevisionCount(revisionList.size());
            }
        }catch (Exception e){
            disease.setScorn(true);
            logger.error("Error getPageIdAndTheirSpecificRevisionByTitleAndSnapshot: pageTitle:" + disease.getName(), e);
        }
        return page;
    }


    public List<Revision> removeRepetedRevision(List<Revision> elements){
        List<Revision> resList = elements;
        Set<Revision> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements);
        elements.clear();
        elements.addAll(linkedHashSet);
        return resList;
    }


    public Long getNumberOfCharactersOfAllTextsFromARevision(String htmlText){
        Long characterCount = 0L;
        Long paragraphCharacterCount = 0L;
        Long tableCharacterCount = 0L;
        Long listCharacterCount = 0L;
        Long imgCharacterCount = 0L;
        org.jsoup.nodes.Document doc = Jsoup.parse(htmlText);

        Elements paragraphs = doc.getElementsByTag(Constants.HTML_P);
        Elements tables = doc.getElementsByTag(Constants.HTML_TABLE);
        Elements ulElements = doc.getElementsByTag(Constants.HTML_UL);
        Elements olElements = doc.getElementsByTag(Constants.HTML_OL);
        Elements dlElements = doc.getElementsByTag(Constants.HTML_DL);
        //todos los caption de imagenes, exepto la del infobox,
        //que esa ya se obtiene en "tables"
        Elements imgElements = doc.select(Constants.QUERY_DIV_CLASS + "thumbcaption" + Constants.RIGHT_PARENTHESIS);

        for (Element paragraph: paragraphs) {
            paragraphCharacterCount = paragraphCharacterCount + (long) paragraph.text().length();
        }
        for (Element table: tables) {
//            System.out.println(table.text() + " - " + table.ownText());
            tableCharacterCount = tableCharacterCount + (long) table.text().length();
        }
        for (Element ulElement: ulElements) {
            listCharacterCount = listCharacterCount + (long) ulElement.text().length();
        }
        for (Element olElement: olElements) {
            listCharacterCount = listCharacterCount + (long) olElement.text().length();
        }
        for (Element dlElement: dlElements) {
            listCharacterCount = listCharacterCount + (long) dlElement.text().length();
        }
        for (Element imgElement: imgElements) {
            imgCharacterCount = imgCharacterCount + (long) imgElement.text().length();
        }


//        logger.info("====================================================== ( PARA:"+paragraphCharacterCount+" TBL:"+tableCharacterCount+" LIST:"+listCharacterCount + " IMG:" + imgCharacterCount + " ) => " + (paragraphCharacterCount + tableCharacterCount + listCharacterCount + imgCharacterCount) );

        characterCount = (paragraphCharacterCount + tableCharacterCount + listCharacterCount + imgCharacterCount);

//        System.out.println("HTML_PARSE => " + doc.outerHtml());

        return characterCount;
    }


    public String getRevisionTextAndSectionList(Revision revision){
        Common common = new Common();
        String text = "";
        try {

            String responseWikipediaAPI = getRevisionTextAndSectionsWikipediaApiQueryResponse(revision.getRevid());
//            System.out.println("Wikipedia API response = " + responseWikipediaAPI);

            //Parser string response Wikipedia API to Java JSON object
            JsonElement jsonElement = parseWikipediaResponse(responseWikipediaAPI);
            //Get information from Json object
            JsonElement parse = jsonElement.getAsJsonObject().get("parse");
            JsonElement sections = jsonElement.getAsJsonObject().get("parse").getAsJsonObject().get("sections");
            //Obtiene todos los elementos de un JsonElement en forma de mapa
            Set<Map.Entry<String, JsonElement>> parseElements = parse.getAsJsonObject().entrySet();
            JsonArray sectionElements = sections.getAsJsonArray();


            //Valida que el mapa no sea nulo /// MAS CAMBIOS
            if (parseElements!=null) {
                for (Map.Entry<String, JsonElement> parseElement : parseElements) {
//                    System.out.println( parseElement.getKey()+ " <-> " + parseElement.getValue());
                    getTextAndSetInRevisionObject(revision, parseElement);
                }
//                System.out.println("    TEXT: " + revision.getText());
            }

            if (sectionElements!=null){
                List<Section> sectionList = new ArrayList<>();
                for (JsonElement sectionElement : sectionElements) {
                    JsonObject sectionObj = sectionElement.getAsJsonObject();
                    Integer toclevel = (sectionObj.get("toclevel") instanceof JsonNull)?0:(sectionObj.get("toclevel").getAsInt());
                    String level = sectionObj.get("level").getAsString();
                    String line = sectionObj.get("line").getAsString();
                    String number = sectionObj.get("number").getAsString();
                    String index = sectionObj.get("index").getAsString();
                    String fromtitle = (common.isEmpty(sectionObj.get("fromtitle").getAsString()))?"":(sectionObj.get("fromtitle").getAsString());//Este es un ejemplo de que algunos elementos no son devueltos o no existen. Ver Hepatomegaly
                    Integer byteoffset = (sectionObj.get("byteoffset") instanceof JsonNull)?0:(sectionObj.get("byteoffset").getAsInt());
                    String anchor = sectionObj.get("anchor").getAsString();

                    Section section = new Section(toclevel, level, line, number, index, fromtitle, byteoffset, anchor);
                    sectionList.add(section);
                }
                revision.setSections(sectionList);
                revision.setSectionCount(sectionList.size());
                revision.setCharacterCount(getNumberOfCharactersOfAllTextsFromARevision(revision.getText()));
//                System.out.println(revision.getRevid());
            }
            //Inserta el número de referencias encontrado
            if (revision.getText()!=null){
                List<Reference> references = extracReferences(revision.getText());
                revision.setReferenceCount(references.size());
            }

        }catch (Exception e){
            logger.error("Error to get revision text Wikipedia API. Revision: " + revision, e);
        }
        return text;
    }


    public Page getRedirectInfo(Disease disease){
        Page page = null;

        return page;
    }


    public void getTextAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        if (Constants.REVISIONS_ELEMENT_TEXT_NAME.equalsIgnoreCase(element.getKey())) revision.setText(element.getValue().getAsString());
    }


    public void getCommentAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        if (Constants.REVISIONS_ELEMENT_COMMENT_NAME.equalsIgnoreCase(element.getKey())) revision.setComment(element.getValue().getAsString());
    }


    public void getSizeAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        if (Constants.REVISIONS_ELEMENT_SIZE_NAME.equalsIgnoreCase(element.getKey())) revision.setSize(element.getValue().getAsInt());
    }


    public void getTimestampAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        TimeProvider timeProvider = new TimeProvider();
        if (Constants.REVISIONS_ELEMENT_TIMESTAMP_NAME.equalsIgnoreCase(element.getKey())) {
            revision.setTimestamp(element.getValue().getAsString());
            revision.setDate(element.getValue().getAsString().substring(0, 10));
        }
    }


    public void getUserIdAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        if (Constants.REVISIONS_ELEMENT_USERID_NAME.equalsIgnoreCase(element.getKey())) revision.setUserid(element.getValue().getAsInt());
    }


    public void getUserAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        if (Constants.REVISIONS_ELEMENT_USER_NAME.equalsIgnoreCase(element.getKey())) revision.setUser(element.getValue().getAsString());
    }


    public void getMinorAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        if (Constants.REVISIONS_ELEMENT_MINOR_NAME.equalsIgnoreCase(element.getKey())) revision.setMinor(element.getValue().getAsBoolean());
    }


    public void getParentIdAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        if (Constants.REVISIONS_ELEMENT_PARENTID_NAME.equalsIgnoreCase(element.getKey())) revision.setParentid(element.getValue().getAsInt());
    }


    public void getRevIdAndSetInRevisionObject(Revision revision, Map.Entry<String, JsonElement> element){
        if (Constants.REVISIONS_ELEMENT_REVID_NAME.equalsIgnoreCase(element.getKey())) revision.setRevid(element.getValue().getAsInt());
    }


    public void getPageIdAndSetInPageObject(Page page, Map.Entry<String, JsonElement> element){
        if (page.getPageid()==null) {
            if (Constants.PAGES_ELEMENT_PAGEID_NAME.equalsIgnoreCase(element.getKey()))
                page.setPageid(element.getValue().getAsInt());
        }
    }


    public void getPageTitleAndSetInPageObject(Page page, Map.Entry<String, JsonElement> element){
        if (page.getTitle()==null) {
            if (Constants.PAGES_ELEMENT_TITLE_NAME.equalsIgnoreCase(element.getKey()))
                page.setTitle(element.getValue().getAsString());
        }
    }


    public void getPageRedirectInfoAndSetInPageObject(Page page, Map.Entry<String, JsonElement> element){
        Common common = new Common();
        if (common.isEmpty(page.getRedirectpagetitle())) {
            if (Constants.PAGES_ELEMENT_TITLE_NAME.equalsIgnoreCase(element.getKey()))
                page.setTitle(element.getValue().getAsString());
        }
    }


    public String deleteFirstAndLastChar(String str){
        return str.substring(1, str.length()-1);
    }


    public JsonElement parseWikipediaResponse(String wikipediaResponse){
        JsonElement jsonElement = null;
//        try {
            jsonElement = new JsonParser().parse(
                    new InputStreamReader(
                            new ByteArrayInputStream(
                                    wikipediaResponse.getBytes(StandardCharsets.UTF_8)
                            )
                    )
            );
//        }catch (Exception e){
//            logger.error("Error parser String Wikipedia response", e.getMessage());
//        }
        return jsonElement;
    }


    public String getWikipediaApiQueryResponse(String titleArticle, String snapshot) throws MalformedURLException {
        //title: Blue rubber bleb nevus syndrome
        //snapshot: 2018-02-15
        String response = "";
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&prop=revisions|redirects&format=json&rvprop=ids|flags|timestamp|userid|user|size|comment&rvstart=" + snapshot + "T00:00:00Z&rvdir=older&rvlimit=1&redirects&titles=" + titleArticle.replace(" ", Constants.BLANK_SPACE_CODE));
            response = getResponseBody(url);
        }catch (Exception e){
            logger.error("Error to make Wikipedia API URL", e);
        }
        return response;
    }


    public String getResponseBody(URL url){
        String response = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
            String line = null;
            while (null != (line = br.readLine())) {
                line = line.trim();
                if (true) {
                    response += line;
                }
            }
        } catch (Exception e){
            logger.error("Error to get data with the Wikipedia API", e);
        }
        return response;
    }


    public String getRevisionTextAndSectionsWikipediaApiQueryResponse(Integer revisionId){
        String response = "";
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=parse&format=json&formatversion=2&prop=sections|text&oldid=" + revisionId);
            response = getResponseBody(url);
        }catch (Exception e){
            logger.error("Error to make Wikipedia API URL (action parse revision data)", e);
        }
        return response;

    }


    public void getDiseasesInfoAndPopulateTheDBProcedure() throws IOException {
        Common common = new Common();
        TimeProvider timeProvider = new TimeProvider();
//        File dir = new File(Constants.ANALYSIS_HISTORY_DIRECTORY);
        File dir = new File("tmp/last_diseases/");
        File[] directoryListing = dir.listFiles();
        String sqlFileSectionTableReport = timeProvider.getNowFormatyyyyMMdd() + "_wikipedia_updates_new_tbl_disease_section_list.sql";
        String pathSqlFileSectionTableReport = "tmp/analysis_result/" + sqlFileSectionTableReport;

        FileWriter fileWriterSqlFileSectionTableReport = new FileWriter(pathSqlFileSectionTableReport);

//        List<Disease> diseases = new ArrayList<Disease>(){{
//            add(new Disease("DIS002645", "Pediatric acute-onset neuropsychiatric syndrome (PANS)"));
//            add(new Disease("DIS003200", "Alpha 1-antitrypsin deficiency"));
//            add(new Disease("DIS003545", "Diabetes mellitus type 1"));
//            add(new Disease("DIS004064", "Diabetes mellitus"));
//            add(new Disease("DIS004098", "Erythema chronicum migrans"));
//            add(new Disease("DIS004879", "Chlamydia infection"));
//            add(new Disease("DIS005124", "Diabetes mellitus type 2"));
//            add(new Disease("DIS005383", "Poliomyelitis"));
//            add(new Disease("DIS006298", "Paraproteinemia"));
//            add(new Disease("DIS006481", "Cri du chat"));
//            add(new Disease("DIS006526", "49,XXXXX"));
//            add(new Disease("DIS006848", "Thrombocytosis"));
//            add(new Disease("DIS007260", "Idiopathic CD4+ lymphocytopenia"));
//            add(new Disease("DIS007413", "Cornelia de Lange Syndrome"));
//            add(new Disease("DIS007485", "Essential thrombocytosis"));
//            add(new Disease("DIS007956", "Pertussis"));
//            add(new Disease("DIS010801", "Neonatal diabetes mellitus"));
//        }};

        int count = 1;
        if (directoryListing != null) {
            int total = directoryListing.length-1;
            for (File diseaseFile : directoryListing) {
                if (!diseaseFile.getName().equalsIgnoreCase(".DS_Store")) {
                    try {
                        Disease jsonFileDisease = common.readDiseaseJSONFileAnalysis(diseaseFile);
                        if (!jsonFileDisease.isScorn()) {
                            logger.info(count + " to " + total + ". (" + jsonFileDisease.isScorn() + ") " + jsonFileDisease.getId() + " - " + jsonFileDisease.getName() + ": revisions: " + jsonFileDisease.getPage().getRevisions().size());
                            for (Revision revision : jsonFileDisease.getPage().getRevisions()) {
                                if (revision.getSections()!=null) {
                                    int countSectionLevelTwo = 0;
                                    for (Section section : revision.getSections()) {
                                        if(Integer.parseInt(section.getLevel())==2){
                                            countSectionLevelTwo=countSectionLevelTwo+1;
                                        }
                                    }
                                    revision.setRelevantSectionCount(countSectionLevelTwo);
//                                    System.out.println("Number of section level 2: " + revision.getRelevantSectionCount());
                                }else{
                                    logger.error("Error, NO tiene Section:" + jsonFileDisease.getId() + " => " +jsonFileDisease.getName());
                                }
                            }

                            Revision previousRevision = null;
                            for (Snapshot snapshot: jsonFileDisease.getSnapshots()) {
                                Revision revision = getRevisionBySnapshot(jsonFileDisease.getPage().getRevisions(), snapshot.getRevId());
//                                System.out.println(revision.getRevid() + " - " + revision.getUser());
                                //Creación de los mysql scripts
                                String sql = "";
                                if (previousRevision==null) {
                                    sql = "update new_tbl_disease_section_list set real_all_section_count = " + revision.getRelevantSectionCount() + " , var_real_all_section_count = " + null + " , total_char_count = " + revision.getCharacterCount() + " , var_total_char_count = " + null + " where disease_id = '" + jsonFileDisease.getId() + "' and actual_snapshot = '" + snapshot.getSnapshot() + "';";
                                }else{
                                    sql = "update new_tbl_disease_section_list set real_all_section_count = " + revision.getRelevantSectionCount() + " , var_real_all_section_count = " + (revision.getRelevantSectionCount() - previousRevision.getRelevantSectionCount()) + " , total_char_count = " + revision.getCharacterCount() + " , var_total_char_count = " + (revision.getCharacterCount() - previousRevision.getCharacterCount()) + " where disease_id = '" + jsonFileDisease.getId() + "' and actual_snapshot = '" + snapshot.getSnapshot() + "';";
                                }
//                                System.out.println(sql);
                                fileWriterSqlFileSectionTableReport.write(sql);
                                previousRevision = revision;
                            }

                            count++;
                        }else{
//                            String noRelevantSQL = "UPDATE new_tbl_disease_list SET relevant = 0 WHERE disease_id = '" + jsonFileDisease.getId() + "';";
//                            System.out.println(noRelevantSQL);
                        }
                    }//END try
                    catch (Exception exception) {
                        logger.error("File " + diseaseFile.getAbsolutePath() + " is not OLE");
                    }//END catch
                }
            }//END for (File file : directoryListing) {
        }//END if (directoryListing != null) {
        fileWriterSqlFileSectionTableReport.close();
    }


    public void getDiseasesInfoAndPopulateTheDBProcedure_REFERENCES() throws IOException {
        Common common = new Common();
        TimeProvider timeProvider = new TimeProvider();
        File dir = new File(Constants.ANALYSIS_HISTORY_DIRECTORY);
        File[] directoryListing = dir.listFiles();
        String sqlFileSectionTableReport = timeProvider.getNowFormatyyyyMMdd() + "_wikipedia_updates_new_tbl_disease_url_list.sql";
        String pathSqlFileSectionTableReport = "tmp/analysis_result/" + sqlFileSectionTableReport;
        FileWriter fileWriterSqlFileSectionTableReport = new FileWriter(pathSqlFileSectionTableReport);

        int count = 1;
        if (directoryListing != null) {
            int total = directoryListing.length-1;
            for (File diseaseFile : directoryListing) {
                if (!diseaseFile.getName().equalsIgnoreCase(".DS_Store")) {
                    try {
                        Disease jsonFileDisease = common.readDiseaseJSONFileAnalysis(diseaseFile);
                        if (!jsonFileDisease.isScorn()) {
                            logger.info(count + " to " + total + ". (" + jsonFileDisease.isScorn() + ") " + jsonFileDisease.getId() + " - " + jsonFileDisease.getName() + ": revisions: " + jsonFileDisease.getPage().getRevisions().size());
                            for (Revision revision : jsonFileDisease.getPage().getRevisions()) {
                                if (!common.isEmpty(revision.getText())) {
                                    List<Reference> referenceList = extracReferences(revision.getText());
                                    revision.setReferenceCount(referenceList.size());
                                    System.out.println("Number of references: " + revision.getReferenceCount());
                                }else{
                                    logger.error("Error, NO tiene Section:" + jsonFileDisease.getId() + " => " +jsonFileDisease.getName());
                                }
                            }

                            Revision previousRevision = null;
                            for (Snapshot snapshot: jsonFileDisease.getSnapshots()) {
                                Revision revision = getRevisionBySnapshot(jsonFileDisease.getPage().getRevisions(), snapshot.getRevId());
//                                System.out.println(revision.getRevid() + " - " + revision.getUser());
                                //Creación de los mysql scripts
                                String sql = "";
                                if (previousRevision==null) {
                                    sql = "update new_tbl_disease_url_list set reference_count = " + revision.getReferenceCount() + " , var_reference_count = " + null + " where disease_id = '" + jsonFileDisease.getId() + "' and actual_snapshot = '" + snapshot.getSnapshot() + "';";
                                }else{
                                    sql = "update new_tbl_disease_url_list set reference_count = " + revision.getReferenceCount() + " , var_reference_count = " + (revision.getReferenceCount() - previousRevision.getReferenceCount()) +  " where disease_id = '" + jsonFileDisease.getId() + "' and actual_snapshot = '" + snapshot.getSnapshot() + "';";
                                }
//                                System.out.println(sql);
                                fileWriterSqlFileSectionTableReport.write(sql);
                                previousRevision = revision;
                            }

                            count++;
                        }else{
//                            String noRelevantSQL = "UPDATE new_tbl_disease_list SET relevant = 0 WHERE disease_id = '" + jsonFileDisease.getId() + "';";
//                            System.out.println(noRelevantSQL);
                        }
                    }//END try
                    catch (Exception exception) {
                        logger.error("File " + diseaseFile.getAbsolutePath() + " is not OLE");
                    }//END catch
                }
            }//END for (File file : directoryListing) {
        }//END if (directoryListing != null) {
        fileWriterSqlFileSectionTableReport.close();
    }


    public void analysisAboutToTheJSONDiseases() throws IOException {
        Common common = new Common();
        TimeProvider timeProvider = new TimeProvider();
        File dir = new File(Constants.ANALYSIS_HISTORY_DIRECTORY);
        File[] directoryListing = dir.listFiles();
        String sqlFileSectionTableReport = timeProvider.getNowFormatyyyyMMdd() + "_wikipedia_diseases_characters.csv";
        String pathSqlFileSectionTableReport = "tmp/analysis_result/" + sqlFileSectionTableReport;
        FileWriter fileWriterSqlFileSectionTableReport = new FileWriter(pathSqlFileSectionTableReport);

        int count = 0;
        if (directoryListing != null) {
            int total = directoryListing.length-1;
            String head = "disease_id,disease_name";
            for (File diseaseFile : directoryListing) {
                if (!diseaseFile.getName().equalsIgnoreCase(".DS_Store")) {
                    try {
                        Disease jsonFileDisease = common.readDiseaseJSONFileAnalysis(diseaseFile);
                        if (!jsonFileDisease.isScorn()) {
                            count++;
                            logger.info(count + " to " + total + ". (" + jsonFileDisease.isScorn() + ") " + jsonFileDisease.getId() + " - " + jsonFileDisease.getName() + ": Num Snapshots: " + jsonFileDisease.getSnapshots().size());

                            if (count==1){
                                int snapCount = 0;
                                for (String trueSnapshot: Constants.ANALYSIS_SNAPSHOT_LIST) {
                                    snapCount++;
                                    head = head + Constants.COMMA + snapCount + "_" + trueSnapshot;
                                }
                                fileWriterSqlFileSectionTableReport.write(head + "\n");
                            }
                            String csvString = "";
                            int snapshotCount = 0;
                            for (String trueSnapshot: Constants.ANALYSIS_SNAPSHOT_LIST) {

                                snapshotCount++;
                                Snapshot snapshot = buscarSnapshot(trueSnapshot, jsonFileDisease.getSnapshots());
                                if (snapshot!=null){
                                    Revision revision = getRevisionBySnapshot(jsonFileDisease.getPage().getRevisions(), snapshot.getRevId());
                                    if (snapshotCount==1){
                                        csvString = csvString + revision.getCharacterCount();
                                    }else{
                                        csvString = csvString + Constants.COMMA + revision.getCharacterCount() ;
                                    }
                                }else{
                                    if (snapshotCount==1){
                                        csvString = csvString + 0;
                                    }else{
                                        csvString = csvString + Constants.COMMA + 0;
                                    }
                                }

                            }
                            String row = jsonFileDisease.getId() + Constants.COMMA + Constants.QUOTATION_MARKS + jsonFileDisease.getName() + Constants.QUOTATION_MARKS + Constants.COMMA + csvString;
//                            System.out.println(row);
                                fileWriterSqlFileSectionTableReport.write(row + "\n");



                        }else{
//                            String noRelevantSQL = "UPDATE new_tbl_disease_list SET relevant = 0 WHERE disease_id = '" + jsonFileDisease.getId() + "';";
//                            System.out.println(noRelevantSQL);
                        }
                    }//END try
                    catch (Exception exception) {
                        logger.error("File " + diseaseFile.getAbsolutePath() + " is not OLE");
                    }//END catch
                }
            }//END for (File file : directoryListing) {
        }//END if (directoryListing != null) {
        fileWriterSqlFileSectionTableReport.close();


        System.out.println("True number of diseases: " + count);
    }


    public Snapshot buscarSnapshot(String findSnapshot, List<Snapshot> snapshots) {
        Optional<Snapshot> snapshot = snapshots.stream()
                .filter(p -> p.getSnapshot().equals(findSnapshot))
                .findFirst();
        return snapshot.isPresent() ? snapshot.get() : null;
    }



    public Revision getRevisionBySnapshot(List<Revision> revisions, Integer revisionId){
        return revisions.stream()
                .filter(revision -> revisionId.equals(revision.getRevid()))
                .findAny()
                .orElse(null);
    }


    public void readDiseaseJSONFile(){

    }


    public Map<Integer, Disease> findErrorsInTheLog(){
        List<String> diseaseErrorList = new ArrayList<>();
        List<String> logLines = getLogLines();
        Map<Integer, Disease> diseaseLog = new HashMap<Integer, Disease>();
        boolean existError = false;
        String previousLine = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    Constants.ANALYSIS_HISTORY_DIRECTORY + "diseases_impl"));
            String line = reader.readLine();
            int count = 1;
            while (line != null) {
//                System.out.println(line);
                if(line.contains(": Error")){
//                    System.out.println(count + ". " + line);
//                    System.out.println(count + ". " + previousLine);
                    if (!previousLine.isEmpty()) {
//                        System.out.println(count + "    " + previousLine);
                        String[] div = previousLine.split("%\\)\\.");
                        Pattern pattern = Pattern.compile("\\: (.*?)\\.");
                        Matcher matcher = pattern.matcher(div[0]);
                        Integer numberLine = Integer.valueOf( ((matcher.find())?matcher.group(1):"n/a") );

                        String strFind = " " + numberLine + "_DIS";
                        String diseaseId = getDiseaseIdInLogFile(logLines, strFind);
                        String diseaseName = div[1];
//                        System.out.println(count + ". Logline: " + numberLine + " | Disease: " + diseaseId + " : " + div[1]);

                        Disease disease = new Disease(diseaseId, diseaseName);
                        diseaseLog.put(numberLine, disease);

                        diseaseErrorList.add(div[1]);
                        count++;
                    }
                }

                previousLine = line;
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("Tamaño del mapa: " + diseaseLog.size());
        return diseaseLog;
    }


    public List<String> getLogLines(){
        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    Constants.ANALYSIS_HISTORY_DIRECTORY + "diseases_impl"));
            String line = reader.readLine();
            int count = 1;
            while (line != null) {
//                System.out.println(line);
                lines.add(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    public String getDiseaseIdInLogFile(List<String> lines, String strFind){
        String diseaseId = "";
        for (String line: lines) {
            if (line.contains(strFind)){
                String[] disInfo = line.split("=> ");
                String[] idInfo = disInfo[1].split("_");
                diseaseId = idInfo[1];
            }
        }
        return diseaseId;
    }

//
//
//    SECCIÓN QUE PERMITE OBTENER EL NÚMERO DE REFERENCIAS
//
//


    public List<Reference> extracReferences(String htmlText){
        org.jsoup.nodes.Document webDocument = Jsoup.parse(htmlText);

        List<Reference> referenceList = new ArrayList<>();
        //En busca de las referencias
        Elements referenceElements = webDocument.getElementsByClass("references").select(Constants.HTML_LI); // .select("");
        if (referenceElements!=null) {
            int refCount = 1;
            for (Element liElement : referenceElements) {
                Reference reference = new Reference();

                reference.setId(refCount);
                reference.setReferenceId(liElement.id());
                reference.setType(getReferenceType(liElement));
                reference.setText(getReferenceText(liElement));
                reference.setTextLinks(getReferenceLinks(liElement));
                reference.setBackLinks(getBackLinkList(liElement));
//                System.out.println(reference);
                referenceList.add(reference);

                refCount++;
            }
        }

        return referenceList;

    }


    public String getReferenceType(Element liElement){
        Elements cites = liElement.getElementsByTag(Constants.HTML_CITE);
        String refType = "";
//        System.out.println(cites.text());
        if (!cites.text().equals("")) {
            for (Element cite : cites) {
                refType = cite.className();break;
            }
        }else{
//            System.out.println(liElement.text());
            Elements spanCite = liElement.getElementsByClass("reference-text");
            for (Element cite : spanCite) {
                refType = cite.className();break;
            }
        }
        return refType;
    }


    public String getReferenceText(Element liElement){
        Elements cites = liElement.getElementsByTag(Constants.HTML_CITE);
        String text = "";
        if (!cites.text().equals("")) {
            for (Element cite: cites) {
                text = cite.text();break;
            }
        }else{
            Elements spanCite = liElement.getElementsByClass("reference-text");
            for (Element cite : spanCite) {
                text = cite.text();break;
            }
        }
        return text;
    }


    public List<Link> getReferenceLinks(Element liElement){
        List<Link> linkList = new ArrayList<>();
        Elements cites = liElement.getElementsByTag(Constants.HTML_CITE);
        if (!cites.text().equals("")) {
            for (Element cite : cites) {
//            Elements links = cite.select(Constants.QUERY_A_HREF);
                linkList = getTextUrls(cite);
                break;
            }
        }else{
            Elements spanCite = liElement.getElementsByClass("reference-text");
            for (Element cite : spanCite) {
                linkList = getTextUrls(cite);break;
            }
        }
        return linkList;
    }


    public List<Link> getBackLinkList(Element liElement){
        List<Link> linkList = new ArrayList<>();
        Elements backLinks = liElement.getElementsByClass("mw-cite-backlink");
        for (Element backLink: backLinks) {
//            Elements links = cite.select(Constants.QUERY_A_HREF);
            linkList = getTextUrls(backLink);break;
        }
        return linkList;
    }


    /**
     * Método que recupera información de enlaces encontrados en cualquier bloque "elemento" del documento
     *
     * @param element
     * @return lista de objetos Link
     */
    public List<Link> getTextUrls(Element element){
        List<Link> urlList = new ArrayList<>();
        Link url;

        // Recorre para obtener todos los enlaces de la lista
        Elements aElements = element.select( Constants.QUERY_A_HREF );
        int countUrl = 1;
        for (Element a : aElements) {
            // Crear un enlace "Link"
            url = new Link();
            url.setId( countUrl );
            url.setUrl( a.attr( Constants.QUERY_ABS_HREF ) );// Obtiene la url absoluta
            url.setDescription( a.text() );

            // Agrea un enlace a la lista de enlaces
            urlList.add( url );
//                                            linkTextMap.put(a.text(), a.attr( Constants.QUERY_ABS_HREF ));
            countUrl++;
        }

        return  urlList;
    }



}
