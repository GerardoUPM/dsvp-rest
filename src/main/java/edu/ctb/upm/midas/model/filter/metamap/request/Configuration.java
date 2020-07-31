package edu.ctb.upm.midas.model.filter.metamap.request;
import edu.ctb.upm.midas.constants.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by gerardo on 06/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project metamap_rest
 * @className Configuration
 * @see
 */
public class Configuration {

    @NotNull(message = Constants.ERR_NO_PARAMETER)
    @Size(min = 1, message = Constants.ERR_EMPTY_PARAMETER)
    private List<String> semanticTypes;
    @NotNull(message = Constants.ERR_NO_PARAMETER)
    @Size(min = 1, message = Constants.ERR_EMPTY_PARAMETER)
    private List<String> sources;
    @NotNull(message = Constants.ERR_NO_PARAMETER)
    @Size(min = 1, message = Constants.ERR_EMPTY_PARAMETER)
    private String options;
    private boolean concept_location;
    private int refreshSessionCount;// Valor "0" es para usar el valor por defecto


    public List<String> getSemanticTypes() {return semanticTypes;}

    public void setSemanticTypes(List<String> semanticTypes) {
        this.semanticTypes = semanticTypes;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public boolean isConcept_location() {
        return concept_location;
    }

    public void setConcept_location(boolean concept_location) {
        this.concept_location = concept_location;
    }

    public int getRefreshSessionCount() {
        return refreshSessionCount;
    }

    public void setRefreshSessionCount(int refreshSessionCount) {
        this.refreshSessionCount = refreshSessionCount;
    }
}
