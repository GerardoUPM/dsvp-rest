package edu.ctb.upm.midas.scheduling;

import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.model.filter.common.Consult;
import edu.ctb.upm.midas.service._extract.MayoClinicExtractService;
import edu.ctb.upm.midas.service.filter.tvp.TvpService;
import edu.ctb.upm.midas.service.jpa.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by gerardo on 03/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project get_diseases_list_rest
 * @className ExtractionScheduling
 * @see
 */
@Service
public class TvpScheduling {

    private static final Logger logger = LoggerFactory.getLogger(TvpScheduling.class);


    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private TvpService tvpService;
    @Autowired
    private DocumentService documentService;


//    #########################################################################
//    ## MAYO CLINIC
//    #########################################################################
    @Scheduled(cron = "0 0 0 4 * ?")
    public void tvpForMayoClinicEveryFirstDayOfTheMonth() throws Exception {
        try {
            logger.info("(MayoClinic) => Scheduled task for the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");

            logger.info("(MayoClinic) Scheduled task for the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to insert Mayo Clinic data (tvpForMayoClinicEveryFirstDayOfTheMonth()): " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 0 18 * ?")
    public void tvpForMayoClinicEvery15thDayOfTheMonth() {
        try {
            logger.info("(MayoClinic) Scheduled for the 18th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            logger.info("(MayoClinic) Scheduled for the 18th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to insert Mayo Clinic data (tvpForMayoClinicEvery15thDayOfTheMonth()): " + e.getMessage());
        }
    }

    private void filterMetaMapMedicalTerms(String source) throws Exception {
        Date snapshot = documentService.findLastVersionNative(source);
        Consult consult = new Consult(source, timeProvider.dateFormatyyyyMMdd(snapshot), false);

        String start = timeProvider.getTime();
        tvpService.filter( consult, true);
        System.out.println("Start: " + start + " | End: " + timeProvider.getTime());
    }

}
