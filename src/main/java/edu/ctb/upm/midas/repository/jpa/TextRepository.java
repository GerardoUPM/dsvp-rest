package edu.ctb.upm.midas.repository.jpa;

import edu.ctb.upm.midas.model.jpa.Text;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className TextRepository
 * @see
 */
public interface TextRepository {

    Text findById(String textId);

    Text findByIdQuery(String textId);

    Text findByContentTypeQuery(String contentType);

    Text findByTextQuery(String text);

    Text findByIdNative(String textId);

    Text findByIdNativeResultClass(String textId);

    List<Object[]> findBySourceAndVersionNative(Date version, String source);

    List<Object[]> findByLikeVersionNative(Date version, String strVersion, String source);

    List<Object[]> findTextWithDetails(String source, Date snapshot, String textId);

    Integer getDisnetConceptsCountInAText(String sourceName, String snapshot, String textId, boolean validatedMedicalElement);

    List<Text> findAllQuery();

    void persist(Text text);

    int insertNative(String textId, String contentType, String text);

    int insertNativeUrl(String textId, String urlId);

    boolean deleteById(String textId);

    void delete(Text text);

    Text update(Text text);

    int updateByIdQuery(Text text);
    
}
