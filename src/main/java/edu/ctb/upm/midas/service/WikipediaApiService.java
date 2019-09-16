package edu.ctb.upm.midas.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.ctb.upm.midas.common.util.Common;
import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.constants.Constants;
import edu.ctb.upm.midas.model.wikipediaApi.Disease;
import edu.ctb.upm.midas.model.wikipediaApi.Page;
import edu.ctb.upm.midas.model.wikipediaApi.Revision;
import edu.ctb.upm.midas.service.jpa.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class WikipediaApiService {

    private static final Logger logger = LoggerFactory.getLogger(WikipediaApiService.class);

    private String encoding = "UTF-8";
    @Autowired
    private DocumentService documentService;

    public void init(){
        WikipediaApiService wikipediaApiService = new WikipediaApiService();
        List<Page> pages = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Common common = new Common();
        TimeProvider timeProvider = new TimeProvider();

        List<Disease> diseases = documentService.findAllDistinctArticlesAndSnapshot();
        int count = 1, total = diseases.size();
        if (diseases.size()>0) {
            for (Disease disease : diseases) {
                logger.info(count + ". DISEASE to " + total + " (" + (count*100)/total + ")." + disease.getName() /*+ " | " + disease.getSnapshotId() + " | " + disease.getCurrentSnapshot() + " | " + disease.getPreviousSnapshot()*/);
                List<Revision> revisions = documentService.findAllSnapshotsOfAArticle(disease.getId());
//                for (Revision revision: revisions) {
//                    logger.info("   " + revision.getSnapshotId() + " | " + revision.getSnapshot() + " | " + revision.getPreviousSnapshot());
//                }
                Page page = wikipediaApiService.getPageIdAndTheirSpecificRevisionByTitleAndSnapshot(disease.getName(), revisions);
//                if (page!=null) pages.add(page);
                if (count==5) break;
                count++;
            }
        }
        //Escribir json
        try {
            common.writeAnalysisJSONFile(gson.toJson(pages), timeProvider.getNowFormatyyyyMMdd());
            logger.info("JSON file write successful!");
        }catch (Exception e){logger.error("Error to write the JSON file", e);}
    }

    public Page getPageIdAndTheirSpecificRevisionByTitleAndSnapshot(String pageTitle, List<Revision> revisions){
        Page page = null;
        Revision previousR = null;
        try {
            int revisionCount = 1;
            for (Revision revision: revisions) {
                String responseWikipediaAPI = getWikipediaApiQueryResponse(pageTitle, revision.getSnapshot());
//            System.out.println("Wikipedia API response = " + responseWikipediaAPI);

                //Parser string response Wikipedia API to Java JSON object
                JsonElement jsonElement = parseWikipediaResponse(responseWikipediaAPI);
                //Get information from Json object
                JsonElement pages = jsonElement.getAsJsonObject().get("query").getAsJsonObject().get("pages");
                //Obtiene todos los elementos de un JsonElement en forma de mapa
                Set<Map.Entry<String, JsonElement>> elementPages = pages.getAsJsonObject().entrySet();

                //Valida que el mapa no sea nulo
                if (elementPages!=null) {
                    //Recorre los elementos del mapa que corresponde al elemento con el pageid,
                    // => " {"24811533": "
                    for (Map.Entry<String, JsonElement> elementPage : elementPages) {
//                System.out.println( elementPage.getKey()+ " <-> " + elementPage.getValue());
                        //Obtiene todos los elementos de un JsonElement en forma de mapa
                        Set<Map.Entry<String, JsonElement>> elementPageInfo = elementPage.getValue().getAsJsonObject().entrySet();
                        //Valida que el mapa del elemento pages no sea nulo
                        if (elementPageInfo!=null) {
                            //Recorre los elementos del mapa
                            for (Map.Entry<String, JsonElement> element : elementPageInfo) {
                                if (revisionCount==1) {
                                    //Inicializa el objeto Page
                                    page = new Page();
                                    //Verifica cada elemento para asignar sus valores a los campos correspondientes
                                    //del recien creado objeto Page
                                    getPageIdAndSetInPageObject(page, element);
                                    getPageTitleAndSetInPageObject(page, element);
                                }
//                            System.out.println(element.getKey() + " - " + element.getValue());
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
                                        if (previousR==null){
                                            revision.setPreviousDate("");
                                        }else{
                                            revision.setPreviousDate(previousR.getDate());
                                        }
                                    }//END if (isJsonObject)
                                }//END if compare if the element is kind of "revisions"
                            }//END for that each element of pages element
                        }//END if (elementPageInfo!=null)
                    }
                }//END if (elementPages!=null)

                //Si las fechas son iguales significa que se trata de la misma revisión y por lo tanto
                //el mismo texto de la anterior se debe colocar en la actual revisión
                //con el fin de no hacer llamadas de más a la "Wikipedia API"
                if (revision.getDate().equalsIgnoreCase(revision.getPreviousDate())){
                    //Si es la misma versión copiaré el texto
                    System.out.println("ES LA MISMA REVISIÓN: (" + revision.getDate() + "==" + revision.getPreviousDate() + ") => (" + revision.getSnapshot() + ")");
                    revision.setText(previousR.getText());
                }else{
                    System.out.println("NO ES LA MISMA REVISIÓN: (" + revision.getDate() + "==" + revision.getPreviousDate() + ") => (" + revision.getSnapshot() + ")");
                    getRevisionText(revision);
                }

                revisionCount++;
                previousR = revision;
            }
            if (page!=null) page.setRevisions(revisions);
        }catch (Exception e){
            logger.error("Error", e);
        }
        return page;
    }

    public String getRevisionText(Revision revision){
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
            System.out.println("DELETE => "+deleteFirstAndLastChar(
                    sections.toString()
            ));
            Set<Map.Entry<String, JsonElement>> sectionElements = parseWikipediaResponse(deleteFirstAndLastChar(sections.toString())).getAsJsonObject().entrySet();

            //Valida que el mapa no sea nulo
            if (parseElements!=null) {
                for (Map.Entry<String, JsonElement> parseElement : parseElements) {
//                    System.out.println( parseElement.getKey()+ " <-> " + parseElement.getValue());
                    getTextAndSetInRevisionObject(revision, parseElement);
                }
//                System.out.println("    TEXT: " + revision.getText());
            }

            if (sectionElements!=null){
                for (Map.Entry<String, JsonElement> sectionElement : sectionElements) {
                    System.out.println( sectionElement.getKey()+ " <-> " + sectionElement.getValue());
                }
            }

        }catch (Exception e){
            logger.error("Error to get revision text Wikipedia API", e);
        }
        return text;
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
        if (Constants.PAGES_ELEMENT_PAGEID_NAME.equalsIgnoreCase(element.getKey())) page.setPageid(element.getValue().getAsInt());
    }


    public void getPageTitleAndSetInPageObject(Page page, Map.Entry<String, JsonElement> element){
        if (Constants.PAGES_ELEMENT_TITLE_NAME.equalsIgnoreCase(element.getKey())) page.setTitle(element.getValue().getAsString());
    }


    public String deleteFirstAndLastChar(String str){
        return str.substring(1, str.length()-1);
    }


    public JsonElement parseWikipediaResponse(String wikipediaResponse){
        JsonElement jsonElement = null;
        try {
            jsonElement = new JsonParser().parse(
                    new InputStreamReader(
                            new ByteArrayInputStream(
                                    wikipediaResponse.getBytes(StandardCharsets.UTF_8)
                            )
                    )
            );
        }catch (Exception e){
            logger.error("Error parser String Wikipedia response", e.getMessage());
        }
        return jsonElement;
    }


    public String getWikipediaApiQueryResponse(String titleArticle, String snapshot){
        //title: Blue rubber bleb nevus syndrome
        //snapshot: 2018-02-15
        String response = "";
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&prop=revisions&format=json&rvprop=ids|flags|timestamp|userid|user|size|comment&rvstart=" + snapshot + "T00:00:00Z&rvdir=older&rvlimit=1&titles=" + titleArticle.replace(" ", Constants.BLANK_SPACE_CODE));
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

}