package edu.ctb.upm.midas.service.jpa;

import edu.ctb.upm.midas.model.jpa.Disease;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DiseaseService
 * @see
 */
public interface DiseaseService {

    Disease findById(String diseaseId);

    Disease findByNameNative(String diseaseName);

    Disease findByNameNativeUnrestricted(String diseaseName);

    Disease findByCui(String cui);

    Disease findLastDiseaseQuery();

    String findLastIdNative();

    List<Disease> findAll();

    List<Object[]> findAllBySourceAndVersionNative(String sourceName, Date version);

    List<Disease> findAllBySourceAndVersionNativeDiseases(String sourceName, Date version);

    Object[] findByIdAndSourceAndVersionNative(String diseaseId, String sourceName, Date version);

    Object[] findByCuiAndSourceAndVersionNative(String cui, String sourceName, Date version);

    List<Disease> findBySourceAndVersionAndCode(String sourceName, Date version, String code, String resourceName);

    Disease findOneBySourceAndVersionAndCode(String sourceName, Date version, String code, String resourceName);

    Disease findBySourceAndVersionAndCodeAndDiseaseName(String sourceName, Date version, String code, String resourceName, String diseaseName);

    List<Object[]> findAllWithTheirDISNETTermsBySourceAndSnapshotAndValidated(String sourceName, Date snapshot, boolean validated);

    void save(Disease disease);

    int insertNative(String diseaseId, String name, String cui);

    int insertNativeHasDisease(String documentId, Date date, String diseaseId);

    boolean updateFindFull(Disease disease);

    boolean updateFindPartial(Disease disease);

    boolean deleteById(String diseaseId);

    int updateCuiByIdAndSourceAndVersionNative(String diseaseId, String cui, String sourceName, Date version);

}
