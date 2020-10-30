package edu.ctb.upm.midas.service.jpa.helperNative;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ctb.upm.midas.common.util.Common;
import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.common.util.UniqueId;
import edu.ctb.upm.midas.model.common.document_structure.Doc;
import edu.ctb.upm.midas.model.common.document_structure.Link;
import edu.ctb.upm.midas.model.common.document_structure.Term;
import edu.ctb.upm.midas.model.extraction.pubmed.PubMedDoc;
import edu.ctb.upm.midas.model.jpa.*;
import edu.ctb.upm.midas.service.jpa.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DocumentHelper
 * @see
 */
@Service
public class DocumentHelperNative {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentSetService documentSetService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private PaperUrlService paperUrlService;
    @Autowired
    private PaperTermService paperTermService;
    @Autowired
    private TermService termService;

    @Autowired
    private UrlHelperNative urlHelperNative;
    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private ResourceHelperNative resourceHelperNative;

    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private Common common;
    @Autowired
    private TimeProvider utilDate;

    private static final Logger logger = LoggerFactory.getLogger(DocumentHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    /**
     * @param sourceId
     * @param document
     * @param version
     * @return
     * @throws JsonProcessingException
     */
    public String insert(String sourceId, Doc document, Date version) throws JsonProcessingException {
        String documentId = uniqueId.generateDocument( sourceId, document.getId() );
        //Buscar si la enfermedad de la que habla este documento ya se encuentra insertada
        //Buscar por source, version y nombre de enfermedad. Si encuentra un documento, entonces no
        //insertar nuevo documento, ni nada relacionado con el (enfermedad, textos y códigos)
        //Cambio para la siguiente version 2018-01-15
        if ( documentService.insertNative( documentId, version ) > 0 ) {
            String docId = documentHelperNative.getDocumentId( documentId, version );
            //Inserta la URL del documento si existe
            if (document.getUrl()!=null) insertURL(document, version, documentId, "");
            //Inserta más de una URL para un documento, (e.g. para los documentos MayoClinic)
            if (document.getUrlList()!=null) insertURLs(document, version, documentId);
            //Inserta la relación entre un documento y su fuente
            documentService.insertNativeHasSource( documentId, version, sourceId );
            return documentId;
        }else
            return "";
//        return documentId;
    }


    /**
     * @param sourceId
     * @param document
     * @param version
     * @return
     * @throws JsonProcessingException
     */
    public String createDocumentId(String sourceId, Doc document, Date version) throws JsonProcessingException {
        String documentId = uniqueId.generateDocument( sourceId, document.getId() );
        return documentId;
    }


    /**
     * @param document
     * @param version
     * @param documentId
     * @param documentLink
     * @throws JsonProcessingException
     */
    public void insertURL(Doc document, Date version, String documentId, String documentLink) throws JsonProcessingException {
        Link documentURL = new Link( (common.isEmpty(documentLink))?document.getUrl().getUrl():documentLink );
        Url url = urlHelperNative.findUrl(documentURL.getUrl());
        if (url!=null){
            documentService.insertNativeUrl( documentId, version, url.getUrlId() );
        }else {
            String urlId = urlHelperNative.getSimpleUrlId(documentURL, document.getId());
            documentService.insertNativeUrl(documentId, version, urlId);
        }
    }


    public void insertURLs(Doc document, Date version, String documentId) throws JsonProcessingException {
        for (Link link: document.getUrlList()) {
               insertURL(document, version, documentId, link.getUrl());
        }
    }

    @Transactional
    public String insertPubMedArticles(String sourceId, edu.ctb.upm.midas.model.extraction.pubmed.Doc document, Date version) throws JsonProcessingException {
        String documentId = uniqueId.generateDocument( sourceId, document.getId() );
        //Document existDocument = documentService.findById(new DocumentPK(documentId, utilDate.convertUtilDateToSQLDate(version)));
        Document existDocument = documentService.findById(new DocumentPK(documentId, utilDate.convertUtilDateToSQLDate(version) ));
        if (existDocument==null) {
            if (documentService.insertNative(documentId, version) > 0) {
                //Se genera un identificador del documento para todas las entidades relacionadas con los documentos
                String docId = documentHelperNative.getDocumentId(documentId, version);
                //Insertar papers "document_set"
                insertPapers(document, documentId, version);
                //SI tiene url la inserta
                if (document.getUrl() != null) {
                    Url url = urlHelperNative.findUrl(document.getUrl().getUrl());
                    if (url != null) {
                        documentService.insertNativeUrl(documentId, version, url.getUrlId());
                    } else {
                        String urlId = urlHelperNative.getSimpleUrlId(document.getUrl(), document.getId());
                        documentService.insertNativeUrl(documentId, version, urlId);
                    }
                }
                //inserta la relación entre el documento y la fuente
                documentService.insertNativeHasSource(documentId, version, sourceId);
                return documentId;
            } else
                return "";
        }else{
            //Buscará si tiene papers relacionados
            //Insertar papers "document_set"
            insertPapers(document, documentId, version);
            return existDocument.getDocumentId();
        }
    }


    @Transactional
    public String insertCodeMeSHPubMedArticles(String sourceId, edu.ctb.upm.midas.model.extraction.pubmed.Doc document, Date version) throws JsonProcessingException {
        String documentId = uniqueId.generateDocument( sourceId, document.getId() );
        Document existDocument = documentService.findById(new DocumentPK(documentId, utilDate.convertUtilDateToSQLDate(version) ));
        if (existDocument==null) {
        }else{

        }
        return documentId;
    }


    @Transactional
    public String insertPubMedArticles_2(String sourceId, edu.ctb.upm.midas.model.extraction.pubmed.Doc document, Date version) throws JsonProcessingException {
        String documentId = uniqueId.generateDocument( sourceId, document.getId() );

        insertPapers(document, documentId, version);
        return documentId;
    }


    private void insertPapers(edu.ctb.upm.midas.model.extraction.pubmed.Doc document, String documentId, Date version) throws JsonProcessingException {
        //Recorrer lista de papers, si existen
        if (document.getPaperList()!=null){
            for (PubMedDoc paper: document.getPaperList()) {
                String paperId = paper.getPmID();
                Paper existPaper = paperService.findById(paperId);
                //Si no existe el paper lo inserta
                if (existPaper==null) {
                    String doi = (common.isEmpty(paper.getDoi()))?"":paper.getDoi();
                    String altId = (common.isEmpty(paper.getPmcID()))?"":paper.getPmcID();
                    String title = (common.isEmpty(paper.getTitleText()))?"":paper.getTitleText();//(common.isEmpty(paper.getTitleText()))?"":paper.getTitleText().substring(0, 2990);
                    String authors = (common.isEmpty(paper.getAuthor()))?"":paper.getAuthor();//(common.isEmpty(paper.getAuthor()))?"":paper.getAuthor().substring(0, 2990);
                    String keywords = (common.isEmpty(paper.getKeyWords()))?"":paper.getKeyWords();//(common.isEmpty(paper.getKeyWords()))?"":paper.getKeyWords().substring(0, 2990);
                    //Inserta el paper
                    if (paperService.insertNative(paperId, doi, altId, title, authors, keywords, paper.isHasFreeText()) > 0) {
                        //Insertar url si existe
                        insertUrl(paper, paperId);
                        //Inserta los terminos asociados al paper y los relaciona
                        insertTerms(paper, paperId);
                        //Inserta la relación entre el paper y el documento DISNET "document_set"
                        insertDocumentSet(documentId, version, paperId);
                    }
                }else{
                    //Si ya existe el paper buscará si existe en document_set
                    insertDocumentSet(documentId, version, paperId);
                }
            }
        }
    }


    /**
     * @param documentId
     * @param version
     * @param paperId
     */
    private void insertDocumentSet(String documentId, Date version, String paperId){
        //Busca si ya se encuentra la relación insertada
        DocumentSet existDocumentSet = documentSetService.findById(new DocumentSetPK(documentId, utilDate.convertUtilDateToSQLDate(version), paperId));
        if (existDocumentSet==null){
            documentSetService.insertNative(documentId, version, paperId);
        }
    }


    /**
     * @param paper
     * @param paperId
     */
    private void insertTerms(PubMedDoc paper, String paperId){
        //Si existen los insertaremos
        if (paper.getTerms()!=null){
            //Recorrer los terminos si existen
            for (Term term: paper.getTerms()) {
                //Primero verificar si existe el "resource", si no existe, lo inserta y regresa el id (int)
                int resourceId = resourceHelperNative.insertIfExist(term.getResource().getName().trim());
                //Busca el termino
                edu.ctb.upm.midas.model.jpa.Term existTerm = termService.findByNameQuery(term.getName().trim());
                if (existTerm!=null){
                    //Buscar e insertar relacion paper con term
                    PaperTerm existPaperTerm = paperTermService.findById(new PaperTermPK(paperId, existTerm.getTermId()));
                    //Si no lo encuentra inserta la relación
                    if (existPaperTerm==null){
                        paperTermService.insertNative(paperId, existTerm.getTermId());
                    }
                }else {
                    //Insertar al no existir
                    if (termService.insertNative(resourceId, term.getName().trim()) > 0) {
                        //Después de insertado lo consulto por el nombre para obtener el id
                        edu.ctb.upm.midas.model.jpa.Term foundTerm = termService.findByNameQuery(term.getName().trim());
                        //Buscar e insertar relacion paper con term
                        PaperTerm existPaperTerm = paperTermService.findById(new PaperTermPK(paperId, foundTerm.getTermId()));
                        //Si no lo encuentra inserta la relación
                        if (existPaperTerm==null){
                            paperTermService.insertNative(paperId, foundTerm.getTermId());
                        }
                    }
                }
            }
        }
    }


    /**
     * @param paper
     * @param paperId
     * @throws JsonProcessingException
     */
    private void insertUrl(PubMedDoc paper, String paperId) throws JsonProcessingException {
        //Insertar url si existe
        if (paper.getLink() != null) {
            Url existUrl = urlHelperNative.findUrl(paper.getLink().getUrl());
            if (existUrl != null) {//Si existe la url
                //Busca que no exista la relacion entre url y paper
                PaperUrl existPaperUrl = paperUrlService.findById(new PaperUrlPK(paperId, existUrl.getUrlId()));
                //Inserta la relacion entre url y paper si no existe
                if (existPaperUrl==null) {
                    paperUrlService.insertNative(paperId, existUrl.getUrlId());
                }
            } else {//Si no existe la url, se crea
                //Busca e inserta la url y regresa id de url nueva  o no
                String urlId = urlHelperNative.getUrl(paper.getLink(), paperId);
                //Busca que no exista la relacion entre url y paper
                PaperUrl existPaperUrl = paperUrlService.findById(new PaperUrlPK(paperId, urlId));
                //Inserta la relacion entre url y paper si no existe
                if (existPaperUrl==null) {
                    paperUrlService.insertNative(paperId, urlId);
                }
            }
        }
    }


    /**
     * @param documentId
     * @param version
     * @return
     */
    public String getDocumentId(String documentId, Date version){
        return documentId + ".V" + utilDate.dateFormatyyyyMMdd(version);
    }

    public boolean findDocument(String documentId, Date version){
        Document document = documentService.findById(new DocumentPK(documentId, utilDate.convertUtilDateToSQLDate(version)));
        if (document!=null) return true;
        else return false;
    }

}
