package edu.ctb.upm.disnet.service.helperNative;

import edu.ctb.upm.disnet.common.util.TimeProvider;
import edu.ctb.upm.disnet.common.util.UniqueId;
import edu.ctb.upm.disnet.constants.Constants;
import edu.ctb.upm.disnet.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by gerardo on 03/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className ConfigurationHelper
 * @see
 */
@Component
public class ConfigurationHelper {

    @Autowired
    private ConfigurationService confService;

    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private Constants constants;
    @Autowired
    private TimeProvider timeProvider;


    public void insert(String source, Date version, String tool, String json){
        String configurationId = uniqueId.generateConfiguration(source, timeProvider.dateFormatyyyMMdd(version));
        confService.insertNative(configurationId, Constants.SOURCE_WIKIPEDIA_CODE, version, tool, json);
        System.out.println("Insert configuration ready!...");
    }
}
