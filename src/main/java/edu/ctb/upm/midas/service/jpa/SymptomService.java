package edu.ctb.upm.midas.service.jpa;

import edu.ctb.upm.midas.model.jpa.Symptom;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SymptomService
 * @see
 */
public interface SymptomService {

    Symptom findById(String cui);

    Symptom findByName(String symptomName);

    boolean findHasSemanticTypeIdNative(String cui, String semanticType);

    List<Object[]> findBySourceAndVersionNative(Date version, String source);

    List<Symptom> findAll();

    void save(Symptom symptom);

    int insertNative(String cui, String name);

    int insertInBatch(List<Symptom> entityList);

    boolean updateFindFull(Symptom symptom);

    boolean updateFindPartial(Symptom symptom);

    boolean deleteById(String cui);
}
