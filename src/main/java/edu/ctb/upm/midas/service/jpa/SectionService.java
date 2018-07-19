package edu.ctb.upm.midas.service.jpa;

import edu.ctb.upm.midas.model.jpa.Section;

import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SectionService
 * @see
 */
public interface SectionService {

    Section findById(String sectionId);

    Section findByName(String sectionName);

    Section findByDescriptionQuery(String sectionDesc);

    Section findLastSectionQuery();

    String findLastSectionIdQuery();

    List<Section> findAll();

    void save(Section section);

    int insertNative(String sectionId, String name, String description);

    boolean updateFindFull(Section section);

    boolean updateFindPartial(Section section);

    boolean deleteById(String sectionId);
    
}
