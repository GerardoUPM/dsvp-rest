package edu.ctb.upm.disnet.extraction_client_modules.wikipedia.texts_extraction.api_response.impl;
import edu.ctb.upm.disnet.extraction_client_modules.wikipedia.texts_extraction.api_response.WikipediaTextsExtractionService;
import edu.ctb.upm.disnet.extraction_client_modules.wikipedia.texts_extraction.client.WikipediaTextsExtractionClient;
import edu.ctb.upm.disnet.extraction_client_modules.wikipedia.texts_extraction.model.request.Request;
import edu.ctb.upm.disnet.extraction_client_modules.wikipedia.texts_extraction.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gerardo on 12/02/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project dgsvp-rest
 * @className WikipediaTextsExtractionServiceImpl
 * @see
 */
@Service
public class WikipediaTextsExtractionServiceImpl implements WikipediaTextsExtractionService {

    @Autowired
    private WikipediaTextsExtractionClient wteServiceClient;

    @Override
    public Response getTexts(Request request) {
        return wteServiceClient.getWikipediaTexts(request);
    }

    @Override
    public Response getResources(Request request) {
        return wteServiceClient.getWikipediaResources(request);
    }
}
