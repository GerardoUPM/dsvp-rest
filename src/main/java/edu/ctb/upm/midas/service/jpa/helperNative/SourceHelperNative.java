package edu.ctb.upm.midas.service.jpa.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ctb.upm.midas.model.common.document_structure.Source;
import edu.ctb.upm.midas.service.jpa.SourceService;
import edu.ctb.upm.midas.common.util.Common;
import edu.ctb.upm.midas.common.util.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SourceHelper
 * @see
 */
@Service
public class SourceHelperNative {

    @Autowired
    private SourceService sourceService;
    @Autowired
    private UrlHelperNative urlHelperNative;
    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(SourceHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;



    /**
     * @param source
     * @return
     * @throws Exception
     */
    @Transactional
    public String insertIfExist(Source source) throws Exception{

        edu.ctb.upm.midas.model.jpa.Source sourceEntity = sourceService.findByName( source.getName() );

        if ( sourceEntity != null ){
            return sourceEntity.getSourceId();
        }else{
            String sourceId = getLastId();
            sourceService.insertNative( sourceId, source.getName() );
            if(source.getUrl()!=null) {
                String urlId = urlHelperNative.getUrl(source.getUrl(), sourceId);
                if (!urlId.equals("")) sourceService.insertNativeUrl(sourceId, urlId);
            }

            return sourceId;
        }
    }


    /**
     * @param nameSource
     * @return
     */
    public boolean exist(String nameSource){
        edu.ctb.upm.midas.model.jpa.Source source = sourceService.findByName( nameSource );
        if( source != null )
            return true;
        else
            return false;
    }


    /**
     * @return
     */
    public String getLastId(){

        if (sourceService.findAll().size() > 0){
            String sourceId = sourceService.findLastSourceIdQuery();

            int last = Integer.parseInt( common.cutStringPerformance(2, 0, sourceId ) );

            return uniqueId.generateSource( last + 1 );

        }else{
            return uniqueId.generateSource( 1 );
        }

    }

}
