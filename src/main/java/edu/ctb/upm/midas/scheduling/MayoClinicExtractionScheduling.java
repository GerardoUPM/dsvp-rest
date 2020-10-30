package edu.ctb.upm.midas.scheduling;

import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.constants.Constants;
import edu.ctb.upm.midas.service._extract.MayoClinicExtractService;
import edu.ctb.upm.midas.service.jpa.DocumentService;
import edu.ctb.upm.midas.service.jpa.TextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
public class MayoClinicExtractionScheduling {

    private static final Logger LOG = LoggerFactory.getLogger(MayoClinicExtractionScheduling.class);

    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private MayoClinicExtractService mayoClinicExtractService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private TextService textService;

    @Scheduled(cron = "0 30 1 1 * ?")
    public void mayoClinicExtractionEveryFirstDayOfTheMonth() throws Exception {
        try {
            LOG.info("(Mayo Clinic) Scheduled task for the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " start.");
            mayoClinicExtractService.extract("", true, false);
            LOG.info("(Mayo Clinic) Scheduled task for the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
        }catch (Exception e){
            LOG.error("Error to insert Mayo Clinic data (mayoClinicExtractionEveryFirstDayOfTheMonth()): " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 22 1 * ?")
    public void mayoClinicExtractionEveryFirstDayOfTheMonthVerification() {
        fixMayoClinicSnapshot();
    }

    @Scheduled(cron = "0 30 1 15 * ?")
    public void mayoClinicExtractionEvery15thDayOfTheMonth() {
        try {
            LOG.info("(Mayo Clinic) Scheduled for the 15th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " start.");
            mayoClinicExtractService.extract("", true, false);
            LOG.info("(Mayo Clinic) Scheduled for the 15th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
        }catch (Exception e){
            LOG.error("Error to insert Mayo Clinic data (mayoClinicExtractionEvery15thDayOfTheMonth()): " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 22 15 * ?")
    public void mayoClinicExtractionEvery15thDayOfTheMonthVerification() {
        fixMayoClinicSnapshot();
    }

    private boolean fixMayoClinicSnapshot(){
        boolean res = false;
        String start = timeProvider.getTime();
        LOG.info("(MayoClinic) Scheduled task for fix the first of each month at midnight " + start + " start.");
        Date lastSnapshot = documentService.findLastVersionNative(Constants.SOURCE_MAYOCLINIC);
        LOG.info("PARAMS => snapshot: " + lastSnapshot + " | " + Constants.SOURCE_MAYOCLINIC);
        List<Object[]> texts = textService.findBySourceAndVersionNative(lastSnapshot, Constants.SOURCE_MAYOCLINIC);

        if (texts==null) {
            LOG.warn("The snapshot of MayoClinic: " + lastSnapshot + " needs to be fixed");
            try {
                boolean fix = mayoClinicExtractService.extract(
                        timeProvider.dateFormatyyyyMMdd(lastSnapshot)
                        , true
                        , false);
                if (fix) {
                    LOG.info("Successful fixing");
                    res = true;
                }
            } catch (Exception e) {
                LOG.error("Error to fixed the MayoClinic snapshot: " + lastSnapshot, e);
            }

        } else {
            LOG.info("The snapshot of MayoClinic: " + lastSnapshot + " is not NULL, is correct");
        }
        LOG.info("(MayoClinic) Scheduled task for fix the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
        LOG.info("Review => Start: " + start + " | End: " + timeProvider.getTime());

        return res;
    }

}
