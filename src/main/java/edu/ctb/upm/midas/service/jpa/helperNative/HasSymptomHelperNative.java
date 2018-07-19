package edu.ctb.upm.midas.service.jpa.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ctb.upm.midas.model.jpa.HasSymptom;
import edu.ctb.upm.midas.model.jpa.HasSymptomPK;
import edu.ctb.upm.midas.service.jpa.HasSymptomService;
import edu.ctb.upm.midas.common.util.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gerardo on 20/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className HasSymptomHelper
 * @see
 */
@Service
public class HasSymptomHelperNative {

    @Autowired
    private HasSymptomService hasSymptomService;

    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(HasSymptomHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;



    /**
     * @param hasSymptomPK
     * @return
     */
    public boolean exist(HasSymptomPK hasSymptomPK){
        HasSymptom hasSymptom = hasSymptomService.findById( hasSymptomPK );
        if( hasSymptom != null )
            return true;
        else
            return false;
    }


}
