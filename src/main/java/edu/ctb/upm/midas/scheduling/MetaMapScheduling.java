package edu.ctb.upm.midas.scheduling;

import edu.ctb.upm.midas.client_modules.filter.metamap.client.MetamapInsertsFeignClient;
import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.constants.Constants;
import edu.ctb.upm.midas.model.filter.common.Consult;
import edu.ctb.upm.midas.service.filter.metamap.MetamapService;
import edu.ctb.upm.midas.service.jpa.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MetaMapScheduling {
    private static final Logger logger = LoggerFactory.getLogger(MetaMapScheduling.class);

    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private MetamapService metamapService;
    @Autowired
    private MetamapInsertsFeignClient metamapInsertsFeignClient;

//    #########################################################################
//    ## WIKIPEDIA
//    #########################################################################
    @Scheduled(cron = "0 0 0 2 * ?")
    public void getMetaMapMedicalTermsInfOnWikipediaSnapshotEveryFirstDayOfTheMonthBy() throws Exception {
        try {
            logger.info("Get MetaMap Medical Terms Procedure (Wikipedia) => Scheduled task for the second of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            boolean resp = callMetaMapMedicalTermFinder(Constants.SOURCE_WIKIPEDIA);
            logger.info("Get MetaMap Medical Terms Procedure (Wikipedia) => Scheduled task for the second of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to Get MetaMap Medical Terms Procedure (Wikipedia) (getMetaMapMedicalTermsInfOnWikipediaSnapshotEveryFirstDayOfTheMonthBy()): " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 0 16 * ?")
    public void getMetaMapMedicalTermsInfOnWikipediaSnapshotEvery15thDayOfTheMonthBy() {
        try {
            logger.info("Get MetaMap Medical Terms Procedure (Wikipedia) => Scheduled for the 16th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            boolean resp = callMetaMapMedicalTermFinder(Constants.SOURCE_WIKIPEDIA);
            logger.info("Get MetaMap Medical Terms Procedure (Wikipedia) => Scheduled for the 16th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to Get MetaMap Medical Terms Procedure (Wikipedia) => (getMetaMapMedicalTermsInfOnWikipediaSnapshotEvery15thDayOfTheMonthBy()): " + e.getMessage());
        }
    }

//    @Scheduled(cron = "0 24 12 2 * ?")
    @Scheduled(cron = "0 0 4 3 * ?")
    public void fastInsertMetaMapMedicalTermsInfOnWikipediaSnapshotEveryFirstDayOfTheMonthBy() throws Exception {
        try {
            logger.info("Batch Insert MetaMap Medical Terms Procedure (Wikipedia) => Scheduled task for the third of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            boolean resp = callMetaMapMedicalTermBatchInsertions(Constants.SOURCE_WIKIPEDIA);
            logger.info("Batch Insert MetaMap Medical Terms Procedure (Wikipedia) => Scheduled task for the third of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to Batch Insert MetaMap Medical Terms Procedure (Wikipedia) => (fastInsertMetaMapMedicalTermsInfOnWikipediaSnapshotEveryFirstDayOfTheMonthBy()): " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 4 17 * ?")
    public void fastInsertMetaMapMedicalTermsInfOnWikipediaSnapshotEvery15thDayOfTheMonthBy() {
        try {
            logger.info("Batch Insert MetaMap Medical Terms Procedure (Wikipedia) => Scheduled for the 17th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            boolean resp = callMetaMapMedicalTermBatchInsertions(Constants.SOURCE_WIKIPEDIA);
            logger.info("Batch Insert MetaMap Medical Terms Procedure (Wikipedia) => Scheduled for the 17th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to Batch Insert MetaMap Medical Terms Procedure (Wikipedia) => (fastInsertMetaMapMedicalTermsInfOnWikipediaSnapshotEvery15thDayOfTheMonthBy()): " + e.getMessage());
        }
    }
//    #########################################################################
//    ## MAYO CLINIC
//    #########################################################################
    @Scheduled(cron = "0 0 12 1 * ?")
    public void getMetaMapMedicalTermsInfOnMayoClinicSnapshotEveryFirstDayOfTheMonthBy() throws Exception {
        try {
            logger.info("Get MetaMap Medical Terms Procedure (MayoClinic) => Scheduled task for the second of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            boolean resp = callMetaMapMedicalTermFinder(Constants.SOURCE_MAYOCLINIC);
            logger.info("Get MetaMap Medical Terms Procedure (MayoClinic) => Scheduled task for the second of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to Get MetaMap Medical Terms Procedure (MayoClinic) (getMetaMapMedicalTermsInfOnMayoClinicSnapshotEveryFirstDayOfTheMonthBy()): " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 12 15 * ?")
    public void getMetaMapMedicalTermsInfOnMayoClinicSnapshotEvery15thDayOfTheMonthBy() {
        try {
            logger.info("Get MetaMap Medical Terms Procedure (MayoClinic) => Scheduled for the 16th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            boolean resp = callMetaMapMedicalTermFinder(Constants.SOURCE_MAYOCLINIC);
            logger.info("Get MetaMap Medical Terms Procedure (MayoClinic) => Scheduled for the 16th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to Get MetaMap Medical Terms Procedure (MayoClinic) => (getMetaMapMedicalTermsInfOnMayoClinicSnapshotEvery15thDayOfTheMonthBy()): " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 16 1 * ?")
    public void fastInsertMetaMapMedicalTermsInfOnMayoClinicSnapshotEveryFirstDayOfTheMonthBy() throws Exception {
        try {
            logger.info("Batch Insert MetaMap Medical Terms Procedure (MayoClinic) => Scheduled task for the third of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            boolean resp = callMetaMapMedicalTermBatchInsertions(Constants.SOURCE_MAYOCLINIC);
            logger.info("Batch Insert MetaMap Medical Terms Procedure (MayoClinic) => Scheduled task for the third of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to Batch Insert MetaMap Medical Terms Procedure (MayoClinic) => (fastInsertMetaMapMedicalTermsInfOnMayoClinicSnapshotEveryFirstDayOfTheMonthBy()): " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 16 15 * ?")
    public void fastInsertMetaMapMedicalTermsInfOnMayoClinicSnapshotEvery15thDayOfTheMonthBy() {
        try {
            logger.info("Batch Insert MetaMap Medical Terms Procedure (MayoClinic) => Scheduled for the 17th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " START.");
            boolean resp = callMetaMapMedicalTermBatchInsertions(Constants.SOURCE_MAYOCLINIC);
            logger.info("Batch Insert MetaMap Medical Terms Procedure (MayoClinic) => Scheduled for the 17th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " END.");
        }catch (Exception e){
            logger.error("Error to Batch Insert MetaMap Medical Terms Procedure (MayoClinic) => (fastInsertMetaMapMedicalTermsInfOnMayoClinicSnapshotEvery15thDayOfTheMonthBy()): " + e.getMessage());
        }
    }

    private boolean callMetaMapMedicalTermFinder(String source) throws ParseException {
        Date snapshot = documentService.findLastVersionNative(source);
        Consult consult = new Consult(source, timeProvider.dateFormatyyyyMMdd(snapshot));
        logger.info("Filter and storage MetaMap result in a JSON file about of: " + consult.toString());
        try {
            metamapService.filterAndStorageInJSON(consult);
            return true;
        } catch (Exception e) {
            logger.error("Error to try call MetaMap");
            return false;
        }
    }

    private boolean callMetaMapMedicalTermBatchInsertions(String source) {
        Date snapshot = documentService.findLastVersionNative(source);
        logger.info("The batch insertion START about of: Source => " + source + ", Snapshot => " + timeProvider.dateFormatyyyyMMdd(snapshot));
        ResponseEntity<HttpStatus> response = metamapInsertsFeignClient.batch(source, timeProvider.dateFormatyyyyMMdd(snapshot));
        if (response.getStatusCode().value() == HttpStatus.OK.value()){
            logger.info(response.toString() + " => successful batch insertion");
            return true;
        }else {
            logger.info(response.toString() + " => errors in the batch insertion");
            return false;
        }
    }

}
