package edu.ctb.upm.midas.repository.jpa;

import edu.ctb.upm.midas.model.jpa.SemanticType;

import java.util.List;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SemanticTypeRepository
 * @see
 */
public interface SemanticTypeRepository {

    SemanticType findById(String semanticType);

    SemanticType findByIdQuery(String semanticType);

    SemanticType findByDescriptionQuery(String description);

    SemanticType findByIdNative(String semanticType);

    SemanticType findByIdNativeResultClass(String semanticType);

    List<SemanticType> findAllQuery();

    void persist(SemanticType semanticType);

    int insertNative(String semanticType, String description);

    int insertNativeHasSemanticType(String cui, String semanticType);

    int insertInBatch(List<SemanticType> entityList);

    boolean deleteById(String semanticType);

    void delete(SemanticType semanticType);

    SemanticType update(SemanticType semanticType);

    int updateByIdQuery(SemanticType semanticType);
    
}
