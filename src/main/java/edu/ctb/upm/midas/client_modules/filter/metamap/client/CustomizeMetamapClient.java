package edu.ctb.upm.midas.client_modules.filter.metamap.client;

import edu.ctb.upm.midas.client_modules.filter.metamap.client.fallback.MetamapClientFallback;
import edu.ctb.upm.midas.configuration.FeignConfiguration;
import edu.ctb.upm.midas.model.filter.metamap.request.Request;
import edu.ctb.upm.midas.model.filter.metamap.response.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by gerardo on 17/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className CustomizeMetamapClient
 * @see //http://localhost:8084
 */
@FeignClient(name = "metamap-client",
        url = "138.4.130.6:11063/api/metamap",
        //url =  "http://localhost:8080/api/metamap",
        fallback = MetamapClientFallback.class,
        configuration = FeignConfiguration.class)
public interface CustomizeMetamapClient {

    @RequestMapping(value = "/filter/json", method = RequestMethod.POST)
    Response filterTexts(@RequestBody Request request);

}
