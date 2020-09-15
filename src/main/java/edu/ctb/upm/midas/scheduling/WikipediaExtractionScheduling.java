package edu.ctb.upm.midas.scheduling;

import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.service.EmailService;
import edu.ctb.upm.midas.service._extract.WikipediaExtractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
public class WikipediaExtractionScheduling {

    private static final Logger logger = LoggerFactory.getLogger(WikipediaExtractionScheduling.class);


    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private WikipediaExtractService wikipediaExtractService;
    @Autowired
    private EmailService emailService;

    /**
     * Método para extraer una nueva lista de enfermedades desde DBPedia y almacenar en la
     * base de datos "addb".
     *
     * Se ejecutará cada día primero de cada mes a la 12:00 horas = @Scheduled(cron = "0 0 12 1 * ? ").
     *
     * Explicación de las expresiones Cron:
     *
     * Dada la siguiente expresión: @Scheduled(cron = "0 9 23 ? * 5 ")
     * La tarea anterior se ejecutará a las 23 horas con 9 minutos y 0 segundos, todos los meses, los días 5 (viernes).

        Las expresiones cron tienen 6 valores obligatorios.

            Segundos. En nuestro ejemplo tiene el valor 0. Acepta valores del 0-59 y caracteres especiales como , - * /
            Minutos. En nuestro ejemplo tiene el valor 9. Acepta valores del 0-59 y caracteres especiales como , - * /
            Hora. En nuestro ejemplo tiene el valor 23. Acepta valores del 0-23 y caracteres especiales como , - * /
            Día del mes. En nuestro ejemplo tiene el caracter especial “?” el cual significa no definido
             ya que no deseamos que se ejecute un determinado día del mes,
             en su lugar deseamos que se ejecute un determinado día de la semana.
             Acepta valores del 1-31 y caracteres especiales como , - * ? /
            Mes. En nuestro ejemplo tiene el caracter especial “*” el cuál significa todos , es decir, deseamos se ejecute todos los meses. Acepta valores del 1-12 o abreviaturas JAN-DEC y caracteres especiales como , - * /
            Día de la semana. En nuestro ejemplo tiene el valor 5, es decir, deseamos se ejecute el quinto día (Viernes). Acepta valores del 1-7 o abreviaturas SUN-SAT y caracteres especiales como , - * ? /
            El día del mes y el día de la semana son excluyentes, es decir que podemos definir solo uno de los dos, no ámbos. En nuestro ejemplo queremos que se ejecute siempre un día de la semana por lo tanto en la posición de día del mes asignaremos un “?” para indicar que no está definido.

            El caracter especial “/” se usa para especificar incrementos.
     Por ejemplo en el campo de minutos, un valor como 0/1 indica que la tarea se ejecutará cada minuto,
     en el campo de segundos un valor como 0/15 indica una ejecución cada 15 segundos.
                Se ejecuta cada minuto de todos los dias sábados a media noche.
                @Scheduled(cron = "0 0/1 0 ? * 6 ")

            El caracter especial “,” se usa para especificar un conjunto de valores.
                Por ejemplo en el campo de día de la semana, un valor como “6,7”
                indica que la tarea se ejecutará todos los sábados y domingos.
                Se ejecuta cada 15 segundos los días sábados y domingos a media noche.
                @Scheduled(cron = "0/15 * 0 ? * 6,7 ")
     */
    @Scheduled(cron = "0 0 4 1 * ?")
    public void wikipediaExtractionEveryFirstDayOfTheMonth() throws Exception {
        try {
//            WikipediaExtractService wikipediaExtractService = new WikipediaExtractService();
            logger.info("(Wikipedia) Scheduled task for the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " start.");
            wikipediaExtractService.extract("", false, false, false);
            logger.info("(Wikipedia) Scheduled task for the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
            EmailService.sendEmail();
        }catch (Exception e){
            logger.error("EIDW_ERR (1stOfTheMonth): " + e.getMessage());
        }
    }


    @Scheduled(cron = "0 0 4 1 * ?")
    public void wikipediaExtractionEveryFirstDayOfTheMonthVerification() {
        /*Primero consultar que hay textos para una sn y fuente (dos partes: la 1a verifica que el anterior proceso ya termino y 2a la verificación de que se insertaron textos)*/

        /*En caso de no tener llamar usar la opción fix de la inserción*/
        /*En caso contrario no realizar ninguna acción*/
    }


//    @Scheduled(cron = "0 0 0 2 * ?")
//    public void completeInsert() { }


    /**
     * Método para extraer una nueva lista de enfermedades desde DBPedia y almacenar en la
     * base de datos "addb".
     *
     * Se ejecutará cada día quince de cada mes a la 12:00 horas = @Scheduled(cron = "0 0 12 15 * ? ").
     */
    //@Scheduled(cron = "0 15 14 15 * ?" )
    //@Scheduled(cron="*/5 * * * * ?")
    @Scheduled(cron = "0 0 4 15 * ?")
    public void wikipediaExtractionEvery15thDayOfTheMonth() {
        try {
//            WikipediaExtractService wikipediaExtractService = new WikipediaExtractService();
            logger.info("(Wikipedia) Scheduled for the 15th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " start.");
            wikipediaExtractService.extract("", false, false, false);
            logger.info("(Wikipedia) Scheduled for the 15th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
            EmailService.sendEmail();
        }catch (Exception e){
            logger.error("(Wikipedia) 15thOfTheMonth: " + e.getMessage());
        }
    }


    public void wikipediaExtraction() throws Exception {
//        try {
//            WikipediaExtractService wikipediaExtractService = new WikipediaExtractService();
            logger.info("(Wikipedia) force scheduled extraction method " + timeProvider.getNowFormatyyyyMMdd() + " start.");
            wikipediaExtractService.extract("", false, false, false);
            logger.info("(Wikipedia) force scheduled extraction method " + timeProvider.getNowFormatyyyyMMdd() + " end.");
//        }catch (Exception e){
//            logger.error("(Wikipedia) force methid: " + e.getMessage());
//        }
    }


//    @Scheduled(cron = "0 0 2 1 * ?")
//    public void mayoclinicExtractionEveryFirstDayOfTheMonth() {
//        try {
//            MayoClinicExtractService mayoClinicExtractService = new MayoClinicExtractService();
//            logger.info("(MayoClinic) Scheduled task for the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " start.");
//            mayoClinicExtractService.extract(timeProvider.getNowFormatyyyyMMdd(), false);
//            logger.info("(MayoClinic) Scheduled task for the first of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
//        }catch (Exception e){
//            logger.error("(MayoClinic) 1stOfTheMonth: " + e.getMessage());
//        }
//    }
//
//
//    @Scheduled(cron = "0 0 2 15 * ?")
//    public void mayoclinicExtractionEvery15thDayOfTheMonth() {
//        try {
//            MayoClinicExtractService mayoClinicExtractService = new MayoClinicExtractService();
//            logger.info("(MayoClinic) Scheduled task for the 15th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " start.");
//            mayoClinicExtractService.extract(timeProvider.getNowFormatyyyyMMdd(), true);
//            logger.info("(MayoClinic) Scheduled task for the 15th of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
//        }catch (Exception e){
//            logger.error("(MayoClinic) 15thOfTheMonth: " + e.getMessage());
//        }
//    }

    //TEST
    //@Scheduled(cron = "0 0 10 17 * ?")
    //@Scheduled(fixedRate = 30000)
    //@Scheduled(cron = "0 15 11 18 * ?" )
    public void test() throws Exception {
        try {
            System.out.println("Tarea programada usando expresiones Cron: 0 0 12 15 * ?" + System.currentTimeMillis() / 1000 + timeProvider.getNowFormatyyyyMMdd());
        }catch (Exception e){
            System.out.println("getAlbumListERR (15thOfTheMonth): " + e.getMessage());
        }
    }

    // Se ejecuta cada 3 segundos
    //@Scheduled(fixedRate = 3000)
    public void tarea1() {
        System.out.println("Tarea usando fixedRate cada 3 segundos - " + System.currentTimeMillis() / 1000 + timeProvider.getNowFormatyyyyMMdd());
    }

}
