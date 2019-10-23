package edu.ctb.upm.midas.service.jpa.helperNative;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ctb.upm.midas.common.util.Common;
import edu.ctb.upm.midas.common.util.UniqueId;
import edu.ctb.upm.midas.model.common.document_structure.code.Code;
import edu.ctb.upm.midas.model.jpa.Resource;
import edu.ctb.upm.midas.model.jpa.RetrievalMethod;
import edu.ctb.upm.midas.model.jpa.SynonymCode;
import edu.ctb.upm.midas.model.jpa.SynonymCodePK;
import edu.ctb.upm.midas.service.jpa.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 13/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className CodeHelper
 * @see
 */
@Service
public class CodeHelperNative {

    @Autowired
    private CodeService codeService;
    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private SynonymService synonymService;
    @Autowired
    private SynonymCodeService synonymCodeService;
    @Autowired
    private UrlHelperNative urlHelperNative;
    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private Common common;

    @Autowired
    private RetrievalMethodService retrievalMethodService;

    private static final Logger logger = LoggerFactory.getLogger(CodeHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    /**
     * @param codeList
     * @param documentId
     * @param version
     * @throws JsonProcessingException
     */
    public void insertIfExistByCodeList(List<Code> codeList, String documentId, Date version, String searchParamCodeRetrievalMethod) throws JsonProcessingException {
        Code existCode;

        if (codeList.size()>0) {
            for (Code code : codeList) {
                existCode = getCodeAndResourceIdByCodeAndResourceName(code);

                if (existCode == null) {//validar el resourceId por si no existe
                    int resourceId = resourceService.findIdByNameQuery(code.getResource().getName());
                    if (resourceId == 0) {
                        //Insertar resource
                        resourceService.insertNative(code.getResource().getName());
                        resourceId = resourceService.findIdByNameQuery(code.getResource().getName());
                    }
                    codeService.insertNative(code.getCode(), resourceId);
                    codeService.insertNativeHasCode(documentId, version, code.getCode(), resourceId);
                    insertRetrievalMethodCode(code.getCode(), resourceId, searchParamCodeRetrievalMethod);
                    if (code.getLink() != null) {
                        String urlId = urlHelperNative.getUrl(code.getLink(), getId(code.getCode(), resourceId));
                        codeService.insertNativeUrl(code.getCode(), resourceId, urlId);
                    }
                } else {
                    //System.out.println("Document_id: " + documentId + " Version: " + version + " Code: " + codeEntity[0] + " ResourceName: " + codeEntity[1]);
                    codeService.insertNativeHasCode(documentId, version, existCode.getCode(), existCode.getResource().getId());
                    insertRetrievalMethodCode(existCode.getCode(), existCode.getResource().getId(), searchParamCodeRetrievalMethod);
                }
            }
        }
    }


    public void insertIfExistByCode(Code code, String documentId, Date version, String searchParamCodeRetrievalMethod) throws JsonProcessingException {
        Code existCode;

        if (code != null) {
            existCode = getCodeAndResourceIdByCodeAndResourceName( code );

            if ( existCode == null ){//validar el resourceId por si no existe
                int resourceId = resourceService.findIdByNameQuery( code.getResource().getName() );
                if (resourceId == 0){
                    //Insertar resource
                    resourceService.insertNative(code.getResource().getName());
                    resourceId = resourceService.findIdByNameQuery( code.getResource().getName() );
                }
                codeService.insertNative( code.getCode(), resourceId );
                codeService.insertNativeHasCode( documentId, version, code.getCode(), resourceId );
                System.out.println("New Code inserted...");
                if (code.getLink()!=null) {
                    String urlId = urlHelperNative.getUrl(code.getLink(), getId(code.getCode(), resourceId));
                    codeService.insertNativeUrl(code.getCode(), resourceId, urlId);
                }
                insertRetrievalMethodCode(code.getCode(), resourceId, searchParamCodeRetrievalMethod);
            }else{
                codeService.insertNativeHasCode( documentId, version, existCode.getCode(), existCode.getResource().getId() );
                System.out.println("Exist Code inserted relation with document...");
            }
        }
    }


    public void insertIfExistSynonymCode(List<Code> codeList, int synonymId, String searchParamCodeRetrievalMethod) throws JsonProcessingException {
        Code existCode;

        for (Code code: codeList) {
            if (code.getResource()!=null) {
                existCode = getCodeAndResourceIdByCodeAndResourceName(code);

                if (existCode == null) {//validar el resourceId por si no existe
                    int resourceId = resourceService.findIdByNameQuery(code.getResource().getName());
                    if (resourceId == 0) {
                        //Insertar resource
                        resourceService.insertNative(code.getResource().getName());
                        resourceId = resourceService.findIdByNameQuery(code.getResource().getName());
                    }
                    if (codeService.insertNative(code.getCode(), resourceId) > 0) {
                        insertRetrievalMethodCode(code.getCode(), resourceId, searchParamCodeRetrievalMethod);
                        SynonymCode existSynonymCode = synonymCodeService.findById(new SynonymCodePK(synonymId, code.getCode(), resourceId));
                        if (existSynonymCode == null) {
                            synonymCodeService.insertNative(synonymId, code.getCode(), resourceId);
                        }
                    }
                    if (code.getLink() != null) {
                        String urlId = urlHelperNative.getUrl(code.getLink(), getId(code.getCode(), resourceId));
                        codeService.insertNativeUrl(code.getCode(), resourceId, urlId);
                    }
                } else {
                    //System.out.println("Document_id: " + documentId + " Version: " + version + " Code: " + codeEntity[0] + " ResourceName: " + codeEntity[1]);
                    //buscar si existe la relacion entre sinonimo y codigo
                    SynonymCode existSynonymCode = synonymCodeService.findById(new SynonymCodePK(synonymId, existCode.getCode(), existCode.getResource().getId()));
                    if (existSynonymCode == null) {
                        synonymCodeService.insertNative(synonymId, existCode.getCode(), existCode.getResource().getId());
                    }
                }
            }
        }
    }


    /**
     *
     * insertar el método de obtención del código
     *                     1: Regular Wikipedia code retrieval (searchParam: wikipedia)
     *                     2: Regular PubMed code retrieval (searchParam: pubmed)
     *                     3: Uts Crosswalk codes retrieval (searchParam: uts crosswalk)
     *
     * @param code
     * @param resourceId
     * @param searchParam
     */
    public void insertRetrievalMethodCode(String code, int resourceId, String searchParam){
        RetrievalMethod retrievalMethod = retrievalMethodService.findByNameNative(searchParam);
//        System.out.println("retrievalMethod: " + retrievalMethod.toString());
        if (retrievalMethod != null){
//            System.out.println("    code: "+ code +" | resourceId:"+ resourceId +" | retrievalMethodId:"+ retrievalMethod.getRetrievalMethodId());
            codeService.insertNativeRetrievalMethod(code, resourceId, retrievalMethod.getRetrievalMethodId());
        }
    }


    /**
     * @param cod
     * @return
     */
    public boolean exist(Code cod){
        Code code = getCodeAndResourceIdByCodeAndResourceName( cod );
        if( code != null )
            return true;
        else
            return false;
    }


    /**
     * @param code
     * @return
     */
    public Code getCodeAndResourceIdByCodeAndResourceName(Code code){
//        System.out.println("RESOURCE NAME A BUSCAR: " + code.getResource().getName());
        Resource existResource = resourceService.findByName( code.getResource().getName() );
        Code existCode = null;
        if (existResource==null){
            if (resourceService.insertNative(code.getResource().getName()) > 0) {
                existResource = resourceService.findByName(code.getResource().getName());
                Object[] codeObject = codeService.findByIdNative(code.getCode(), existResource.getResourceId());
//        (String) codeEntity[0], (int) codeEntity[1]
                if (existResource!=null && codeObject!=null)
                    existCode = new Code((String) codeObject[0], new edu.ctb.upm.midas.model.common.document_structure.code.Resource(existResource.getResourceId(), existResource.getName()));
            }
        }else {
            Object[] codeObject = codeService.findByIdNative(code.getCode(), existResource.getResourceId());
//        (String) codeEntity[0], (int) codeEntity[1]
            if (existResource != null && codeObject != null)
                existCode = new Code((String) codeObject[0], new edu.ctb.upm.midas.model.common.document_structure.code.Resource(existResource.getResourceId(), existResource.getName()));
        }
        return existCode;

    }


    /**
     * @param code       
     * @param resourceId
     * @return
     */
    public String getId(String code, int resourceId) {
        return uniqueId.generateCode( code, resourceId );
    }


}
