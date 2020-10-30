package edu.ctb.upm.midas.service._populate;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ctb.upm.midas.common.util.TimeProvider;
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
public class MayoClinicPopulateDbNative {

    private static final Logger logger = LoggerFactory.getLogger(MayoClinicPopulateDbNative.class);

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private TimeProvider date;




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
            //<editor-fold desc="PERSISTIR TODOS LOS DATOS DE LOS DOCUMENTOS DISNET">
            for (Doc document: source.getDocuments()) {
                if (fix) {
                    insertDocumentDataRestore(document, sourceId, version, source, docsCount, isJSONRequest);
                } else {
                    insertDocumentData(document, sourceId, version, source, docsCount, isJSONRequest);
                }
                docsCount++;
            }// Documentos
            //</editor-fold>
            System.out.println("Inserted Documents: " + docsCount);
            System.out.println("No inserted Documents(invalid): " + invalidCount);
        }// Fuentes "Sources"
        System.out.println("Populate end...");
        //extractionWikipedia.printReport();

    }


    @Transactional
    public void insertDocumentData(Doc document, String sourceId, Date version, Source source, int docsCount, boolean isJSONRequest) throws IOException {
        //Solo inserta aquellos documentos que al menos tengan códigos o secciones
        String documentId = documentHelperNative.insert(sourceId, document, version);

        //<editor-fold desc="PERSISTIR ENFERMEDAD DEL DOCUMENTO">
        String diseaseId = diseaseHelperNative.insertIfExist(document, documentId, version, source);
        //</editor-fold>

        //<editor-fold desc="PERSISTIR CÓDIGOS DEL DOCUMENTO">
        if (document.getCodeList()!=null)
            codeHelperNative.insertIfExistByCodeList(document.getCodeList(), documentId, version, source.getName());
        //</editor-fold>

        //<editor-fold desc="RECORRIDO DE SECCIONES PARA ACCEDER A LOS TEXTOS">
        if (document.getSectionList()!=null) {
            for (Section section : document.getSectionList()) {
                //Si la sección no tiene textos no se inserta en la relación has_section
                if (section.getTextList()!=null) {
                    //<editor-fold desc="PERSISTIR has_section">
                    //inserta la sección para ese documento
                    String sectionId = hasSectionHelperNative.insert(documentId, version, section);
                    //</editor-fold>

                    //Validar si hay textos
                    int textCount = 0;
                    for (Text text : section.getTextList()) {
                        //<editor-fold desc="INSERTAR TEXTO">
//                        System.out.println("Insertar textos");
                        textHelperNative.insert(text, sectionId, documentId, version, isJSONRequest);
                        //</editor-fold>

                        textCount++;
                    }// Textos
                }

            }// Secciones
        }
        //</editor-fold>

//        System.out.println(docsCount + " Insert document: " + document.getDisease().getName() + "_" + documentId + "(" + diseaseId + ")");
        System.out.println(docsCount + " Insert document: " + document.getDisease().getName() + "_" + documentId );
    }

    @Transactional
    public void insertDocumentDataRestore(Doc document, String sourceId, Date version, Source source, int docsCount, boolean isJSONRequest) throws IOException {
        //Genera el documentId ya existente
        String documentId = documentHelperNative.createDocumentId(sourceId, document, version);
        System.out.println(docsCount + " Insert document: " + document.getDisease().getName() + "_" + documentId );

        //<editor-fold desc="RECORRIDO DE SECCIONES PARA ACCEDER A LOS TEXTOS">
        if (document.getSectionList() != null) {
            for (Section section : document.getSectionList()) {
                //Si la sección no tiene textos no se inserta en la relación has_section
                if (section.getTextList() != null) {
                    //<editor-fold desc="PERSISTIR has_section">
                    //inserta la sección para ese documento
                    String sectionId = hasSectionHelperNative.insert(documentId, version, section);
                    //</editor-fold>

                    //Validar si hay textos
                    int textCount = 0;
                    for (Text text : section.getTextList()) {
                        //<editor-fold desc="INSERTAR TEXTO">
//                        System.out.println("Insertar textos");
                        textHelperNative.insert(text, sectionId, documentId, version, isJSONRequest);
                        //</editor-fold>
                        textCount++;
                    }// Textos
                }

            }// Secciones
        }
        //</editor-fold>

//        System.out.println(docsCount + " Insert document: " + document.getDisease().getName() + "_" + documentId + "(" + diseaseId + ")");
        System.out.println(docsCount + " Insert document: " + document.getDisease().getName() + "_" + documentId );
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
