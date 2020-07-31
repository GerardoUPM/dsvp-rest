package edu.ctb.upm.midas.model.filter.metamap.special;
import java.util.Objects;

/**
 * Created by gerardo on 09/05/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className SemanticType
 * @see
 */
public class SemanticType {

    String type;

    public SemanticType() {
    }

    public SemanticType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SemanticType)) return false;
        SemanticType that = (SemanticType) o;
        return Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    @Override
    public String toString() {
        return "SemanticType{" +
                "type='" + type + '\'' +
                '}';
    }
}
