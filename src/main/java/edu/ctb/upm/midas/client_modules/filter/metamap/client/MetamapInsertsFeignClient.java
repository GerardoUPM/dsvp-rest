package edu.ctb.upm.midas.client_modules.filter.metamap.client;

import edu.ctb.upm.midas.client_modules.filter.metamap.client.fallback.MetamapClientFallback;
import edu.ctb.upm.midas.configuration.FeignConfiguration;
import edu.ctb.upm.midas.model.filter.metamap.request.Request;
import edu.ctb.upm.midas.model.filter.metamap.response.Response;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by gerardo on 17/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className MetamapInsertsClient
 * @see
 */
@FeignClient(name = "${my.service.client.metamap.inserts.name}",
        url = "${my.service.client.metamap.inserts.url}",
        configuration = FeignConfiguration.class)
public interface MetamapInsertsFeignClient {

    @RequestMapping(value = "${my.service.client.metamap.inserts.batch.path}", method = RequestMethod.GET)
    ResponseEntity<HttpStatus> batch(@RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,
                                      @RequestParam(value = "snapshot") @Valid @NotBlank @NotNull @NotEmpty String snapshot);

}
