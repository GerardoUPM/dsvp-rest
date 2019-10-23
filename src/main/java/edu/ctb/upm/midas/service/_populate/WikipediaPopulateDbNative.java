package edu.ctb.upm.midas.service._populate;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ctb.upm.midas.client_modules.extraction.wikipedia.disease_list.api_response.DiseaseAlbumResourceService;
import edu.ctb.upm.midas.common.util.Common;
import edu.ctb.upm.midas.common.util.UniqueId;
import edu.ctb.upm.midas.constants.Constants;
import edu.ctb.upm.midas.model.common.document_structure.Doc;
import edu.ctb.upm.midas.model.common.document_structure.Section;
import edu.ctb.upm.midas.model.common.document_structure.Source;
import edu.ctb.upm.midas.model.common.document_structure.code.Resource;
import edu.ctb.upm.midas.model.common.document_structure.text.Text;
import edu.ctb.upm.midas.service.jpa.helperNative.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gerardo on 29/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className PopulateDbNative
 * @see
 */
@Service
public class WikipediaPopulateDbNative {

    private static final Logger logger = LoggerFactory.getLogger(WikipediaPopulateDbNative.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ResourceHelperNative resourceHelperNative;
    @Autowired
    private DiseaseHelperNative diseaseHelperNative;
    @Autowired
    private SourceHelperNative sourceHelperNative;
    @Autowired
    private SectionHelperNative sectionHelperNative;
    @Autowired
    private HasSectionHelperNative hasSectionHelperNative;
    @Autowired
    private UrlHelperNative urlHelperNative;
    @Autowired
    private CodeHelperNative codeHelperNative;
    @Autowired
    private TextHelperNative textHelperNative;
    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private SemanticTypeHelperNative semanticTypeHelperNative;

    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private Constants constants;
    @Autowired
    private Common common;

    @Autowired
    private DiseaseAlbumResourceService diseaseAlbumResource;


    
    /**
     * @throws Exception
     */
    @Transactional
    public void populate(List<Source> sourceList, Date version, boolean isJSONRequest, boolean fix) throws Exception {
        //Date version = dateVersion;//date.getSqlDate();
        System.out.println("-------------------- POPULATE DATABASE --------------------");
        System.out.println("Populate start...");
        for (Source source: sourceList) {
            String sourceId = sourceHelperNative.insertIfExist( source );
            System.out.println("Source: " + sourceId + " - " + source.getName());

            //<editor-fold desc="PERSISTIR TODAS LAS SECCIONES">
            System.out.println("Insert all sections, if exists...");
            sectionHelperNative.insertIfExist( source.getSectionMap() );
            System.out.println("Insert all sections ready!");
            System.out.println("Insert documents start!");
            //</editor-fold>
            int docsCount = 1, invalidCount = 1;
            for (Doc document: source.getDocuments()) {
                //Solo inserta aquellos documentos que al menos tengan códigos o secciones
                if (document.isDiseaseArticle()) {
                    if (fix){
                        insertDocumentDataRestore(document, sourceId, version, source, docsCount, isJSONRequest);
                    }else{
                        insertDocumentData(document, sourceId, version, source, docsCount, isJSONRequest);
                    }
                    docsCount++;
                }else{
                    invalidCount++;
                }
            }// Documentos
            System.out.println("Inserted Documents: " + docsCount);
            System.out.println("No inserted Documents(invalid): " + invalidCount);
        }// Fuentes "Sources"
        System.out.println("Populate end...");
        //extractionWikipedia.printReport();

    }


    public void insertDocumentData(Doc document, String sourceId, Date version, Source source, int docsCount, boolean isJSONRequest) throws IOException {
        String documentId = documentHelperNative.insert(sourceId, document, version);
        logger.info(docsCount + " Insert document: " + document.getDisease().getName() + "_" + documentId );

//        System.out.println(docsCount + " Insert document: " + document.getDisease().getName() + "_" + documentId);

        //<editor-fold desc="PERSISTIR ENFERMEDAD DEL DOCUMENTO">
        String diseaseId = diseaseHelperNative.insertIfExist(document, documentId, version, source);
        //</editor-fold>

        //<editor-fold desc="PERSISTIR CÓDIGOS DEL DOCUMENTO">
        codeHelperNative.insertIfExistByCodeList(document.getCodeList(), documentId, version, source.getName());
        //</editor-fold>

        //<editor-fold desc="RECORRIDO DE SECCIONES PARA ACCEDER A LOS TEXTOS">
        for (Section section : document.getSectionList()) {
            //<editor-fold desc="PERSISTIR has_section">
            String sectionId = hasSectionHelperNative.insert(documentId, version, section);
            //</editor-fold>

            int textCount = 0;
            for (Text text : section.getTextList()) {
//                            System.out.println("Have texts...");
                //<editor-fold desc="INSERTAR TEXTO">
                textHelperNative.insert(text, sectionId, documentId, version, isJSONRequest);
                //</editor-fold>

                textCount++;
            }// Textos

        }// Secciones
        //</editor-fold>
    }


    public void insertDocumentDataRestore(Doc document, String sourceId, Date version, Source source, int docsCount, boolean isJSONRequest) throws IOException {
        String documentId = documentHelperNative.createDocumentId(sourceId, document, version);
        System.out.println(docsCount + " Insert document: " + document.getDisease().getName() + "_" + documentId );

        //<editor-fold desc="RECORRIDO DE SECCIONES PARA ACCEDER A LOS TEXTOS">
        for (Section section : document.getSectionList()) {
            //<editor-fold desc="PERSISTIR has_section">
            String sectionId = hasSectionHelperNative.insert(documentId, version, section);
            //</editor-fold>

            int textCount = 0;
            for (Text text : section.getTextList()) {
//                            System.out.println("Have texts...");
                //<editor-fold desc="INSERTAR TEXTO">
                textHelperNative.insert(text, sectionId, documentId, version, isJSONRequest);
                //</editor-fold>

                textCount++;
            }// Textos

        }// Secciones
        //</editor-fold>
    }


    /**
     * @throws Exception
     */
    @Transactional
    public void populateResource(HashMap<String, Resource> resourceMap) throws Exception {
        System.out.println("Insert resources...");
        List<edu.ctb.upm.midas.model.jpa.Resource> resourceList =
                resourceHelperNative.insertIfExist( resourceMap );

        if ( resourceList.size() > 0 ) System.out.println("Insert resources Ready!");

    }


    /**
     * @throws Exception
     */
    @Transactional //CAMBIAR A NATIVOS
    public void populateSemanticTypes() throws Exception {
        System.out.println("Insert semantic types...");
        for (String semanticType:
                Constants.SEMANTIC_TYPES) {
            semanticTypeHelperNative.insertIfExist( semanticType, "" );
        }
        System.out.println("Insert semantic types Ready!");
    }

}
