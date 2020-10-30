package edu.ctb.upm.midas.controller;


import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.model.filter.common.Consult;
import edu.ctb.upm.midas.service.filter.metamap.MetamapService;
import edu.ctb.upm.midas.service.filter.tvp.TvpService;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by gerardo on 05/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project validation_medical_term
 * @className ValidationController
 * @see
 **/

@RestController
@RequestMapping("/api")
public class FilterController {

    @Autowired
    private MetamapService metamapService;
    @Autowired
    private TvpService tvpService;
    @Autowired
    private TimeProvider utilDate;

    @RequestMapping(path = { "/metamap" },
            method = RequestMethod.GET)
    public String metamapFilter() throws Exception {

        Consult consult = new Consult("_wikipedia",
                "2018-02-01");

        String inicio = utilDate.getTime();
        //metamapService.localFilter( consult );
        metamapService.filter( consult );
        System.out.println("Inicio:" + inicio + " | Termino: " +utilDate.getTime());

        return "It has been successfully filtered with Metamap";
    }


    @RequestMapping(path = { "/metamap/diseases" },
            method = RequestMethod.GET)
    public String metamapFilterDiseasesName() throws Exception {

        Consult consult = new Consult("_wikipedia",
                "2017-09-03");

        metamapService.filterDiseaseName( consult );

        return "It has been successfully filtered DISEASES NAME with Metamap";
    }

    @RequestMapping(path = { "/metamap/json" },
            method = RequestMethod.GET,
            params = {"source", "snapshot", "action"})
    public String metamapFilterWithJSON(@RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,
                                        @RequestParam(value = "snapshot") @Valid @NotBlank @NotNull @NotEmpty String snapshot,
                                        @RequestParam(value = "action") @Valid @NotBlank @NotNull @NotEmpty String action
    ) throws Exception {

        //Consult consult = new Consult("wikipedia", snapshot);//"2018-04-15"
        //Consult consult = new Consult("pubmed", "2018-04-03");
        Consult consult = new Consult(source, snapshot);//"2018-04-15"

        String inicio = utilDate.getTime();
        //Cuando se realice el filtro
        switch (action) {
            case "filter_storage_json":
                //Llama a Metamap y su resultado lo almacena en un JSON tanto en Metamap como en este servicio
                System.out.println("filterAndStorageInJSON: " + consult.toString());
                metamapService.filterAndStorageInJSON(consult);
                break;
            case "populate_json":
                //Cuando se consuma el JSON y se almacene la información
                System.out.println("populateTextsStoredJSON: " + consult.toString());
                metamapService.populateTextsStoredJSON( consult );
                break;
            case "restart_populate_json":
                //Cuando se queda a medias la inserción de los resultados de Metamap
                System.out.println("restartPopulateTextsStoredJSON: " + consult.toString());
                metamapService.restartPopulateTextsStoredJSON( consult );
                break;
            default:
                System.out.println("Invalid action");
                break;
        }
        System.out.println("Inicio:" + inicio + " | Termino: " +utilDate.getTime());

        return "It has been successfully filtered with Metamap";
    }


    /**
     * @param source
     * @param snapshot
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/metamap/json/fast" }, //Term Validation Procedure
            method = RequestMethod.GET,
            params = {"source", "snapshot"})
    public String metamapFilterWithJSON_fast(@RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,
                                             @RequestParam(value = "snapshot") @Valid @NotBlank @NotNull @NotEmpty String snapshot) throws Exception {

        Consult consult = new Consult(source, snapshot);//"2018-04-15"
        String inicio = utilDate.getTime();
        metamapService.createMySQLInserts( consult );
//        metamapService.createMySQLInserts_TEST(consult);//for know text list size
        System.out.println("Inicio:" + inicio + " | Termino: " +utilDate.getTime());

        return "It has been successfully filtered with Metamap";
    }


    /**
     * @param source
     * @param snapshot
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/metamap/json/fast/batch" }, //Term Validation Procedure
            method = RequestMethod.GET,
            params = {"source", "snapshot"})
    public String metamapFilterWithJSONBatch(@RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,
                                             @RequestParam(value = "snapshot") @Valid @NotBlank @NotNull @NotEmpty String snapshot) throws Exception {

        Consult consult = new Consult(source, snapshot);//"2018-04-15"
        String inicio = utilDate.getTime();
        metamapService.insertInBatch( consult );
        System.out.println("Inicio:" + inicio + " | Termino: " +utilDate.getTime());

        return "It has been successfully filtered with Metamap";
    }


    @RequestMapping(path = { "/tvp" }, //Term Validation Procedure
            method = RequestMethod.GET,
            params = {"source", "snapshot", "json"})
    public String tvpValidation(@RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source
                                , @RequestParam(value = "snapshot") @Valid @NotBlank @NotNull @NotEmpty String snapshot
                                , @RequestParam(value = "json") @Valid @NotBlank @NotNull @NotEmpty boolean json
                                , @RequestParam(value = "storage", required = false, defaultValue = "true") boolean storage) throws Exception {

        Consult consult = new Consult(source, snapshot, json);

        String inicio = utilDate.getTime();
        tvpService.filter( consult, storage );
        System.out.println("Inicio:" + inicio + " | Termino: " +utilDate.getTime());

        return "It has been validated filtered with TVP";
    }

}
