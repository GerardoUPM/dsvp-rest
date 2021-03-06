package edu.ctb.upm.midas.repository.jpa;

import edu.ctb.upm.midas.model.jpa.HasDisease;
import edu.ctb.upm.midas.model.jpa.HasDiseasePK;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 23/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className HasDiseaseRepository
 * @see
 */
public interface HasDiseaseRepository {

    HasDisease findById(HasDiseasePK hasDiseasePK);

    HasDisease findByIdQuery(HasDiseasePK hasDiseasePK);

    HasDisease findByIdNative(HasDiseasePK hasDiseasePK);

    HasDisease findByIdNativeResultClass(HasDiseasePK hasDiseasePK);

    List<HasDisease> findAllQuery();

    void persist(HasDisease hasDisease);

    int insertNative(String documentId, Date date, String diseaseId);

    boolean deleteById(HasDiseasePK hasDiseasePK);

    void delete(HasDisease hasDisease);

    HasDisease update(HasDisease hasDisease);

    int updateByIdQuery(HasDisease hasDisease);
    
}
