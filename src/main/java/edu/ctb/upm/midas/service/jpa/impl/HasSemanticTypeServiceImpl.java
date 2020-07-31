package edu.ctb.upm.midas.service.jpa.impl;
import edu.ctb.upm.midas.model.jpa.HasSemanticType;
import edu.ctb.upm.midas.model.jpa.HasSemanticTypePK;
import edu.ctb.upm.midas.repository.jpa.HasSemanticTypeRepository;
import edu.ctb.upm.midas.service.jpa.HasSemanticTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gerardo on 29/06/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className HasSemanticTypeServiceImpl
 * @see
 */
@Service("hasSemanticTypeService")
public class HasSemanticTypeServiceImpl implements HasSemanticTypeService {

    @Autowired
    private HasSemanticTypeRepository daoHasSemanticTypeRepository;

    @Override
    public HasSemanticType findById(HasSemanticTypePK hasSemanticTypePK) {
        return null;
    }

    @Override
    public HasSemanticType findByIdQuery(HasSemanticTypePK hasSemanticTypePK) {
        return null;
    }

    @Override
    public HasSemanticType findByIdNative(HasSemanticTypePK hasSemanticTypePK) {
        return null;
    }

    @Override
    public List<HasSemanticType> findAllQuery() {
        return null;
    }

    @Override
    public void save(HasSemanticType hasSemanticType) {

    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertInBatch(List<HasSemanticType> entityList) {
        return daoHasSemanticTypeRepository.insertInBatch(entityList);
    }

    @Override
    public boolean deleteById(HasSemanticTypePK hasSemanticTypePK) {
        return false;
    }

    @Override
    public void delete(HasSemanticType hasSemanticType) {

    }

    @Override
    public HasSemanticType update(HasSemanticType hasSemanticType) {
        return null;
    }

    @Override
    public int updateByIdQuery(HasSemanticType hasSemanticType) {
        return 0;
    }
}
