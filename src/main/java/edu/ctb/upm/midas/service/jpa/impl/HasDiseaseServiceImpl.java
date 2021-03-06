package edu.ctb.upm.midas.service.jpa.impl;
import edu.ctb.upm.midas.model.jpa.HasDisease;
import edu.ctb.upm.midas.model.jpa.HasDiseasePK;
import edu.ctb.upm.midas.repository.jpa.HasDiseaseRepository;
import edu.ctb.upm.midas.service.jpa.HasDiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 18/04/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className HasDiseaseServiceImpl
 * @see
 */
@Service("hasDiseaseService")
public class HasDiseaseServiceImpl implements HasDiseaseService {

    @Autowired
    private HasDiseaseRepository daoHasDisease;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public HasDisease findById(HasDiseasePK hasDiseasePK) {
        HasDisease hasDisease = daoHasDisease.findById(hasDiseasePK);
        return hasDisease;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<HasDisease> findAll() {
        return null;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(HasDisease hasDisease) {

    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNative(String documentId, Date date, String diseaseId) {
        return 0;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(HasDisease hasDisease, HasDiseasePK hasDiseasePK) {
        return false;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(HasDisease hasDisease, HasDiseasePK hasDiseasePK) {
        return false;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(HasDiseasePK hasDiseasePK) {
        return false;
    }
}
