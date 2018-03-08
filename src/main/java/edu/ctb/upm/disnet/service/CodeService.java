package edu.ctb.upm.disnet.service;

import edu.ctb.upm.disnet.model.jpa.Code;
import edu.ctb.upm.disnet.model.jpa.CodePK;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 13/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className CodeService
 * @see
 */
public interface CodeService {

    Code findById(CodePK codePK);

    Code findByCodeQuery(String code);

    Code findByResourceIdQuery(int resourceId);

    Object[] findByIdNative(String code, int resourceId);
    
    List<Code> findAll();

    void save(Code code);

    int insertNative(String code, int resourceId);

    int insertNativeUrl(String code, int resourceId, String urlId);

    int insertNativeHasCode(String documentId, Date date, String code, int resourceId);

    boolean updateFindFull(Code code, CodePK codePK);

    boolean updateFindPartial(Code code, CodePK codePK);

    boolean deleteById(CodePK codePK);

}
