package edu.ctb.upm.midas.repository.jpa.impl;
import edu.ctb.upm.midas.model.jpa.Configuration;
import edu.ctb.upm.midas.repository.jpa.AbstractDao;
import edu.ctb.upm.midas.repository.jpa.ConfigurationRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 04/09/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className ConfigurationRepositoryImpl
 * @see
 */
@Repository("ConfigurationRepositoryDao")
public class ConfigurationRepositoryImpl extends AbstractDao<String, Configuration>
                                        implements ConfigurationRepository{

    @Override
    public Configuration findById(String configurationId) {
        return null;
    }

    @Override
    public Configuration findByIdQuery(String configurationId) {
        return null;
    }

    @Override
    public Object[] findBySourceNameNative(String sourceName) {
        return new Object[0];
    }

    @Override
    public Object[] findBySourceIdNative(String sourceId) {
        return new Object[0];
    }

    @Override
    public Object[] findByVersionNative(Date version) {
        return new Object[0];
    }

    @Override
    public Object[] findByToolNative(String toolName) {
        return new Object[0];
    }

    @Override
    public Object[] findByContigurationNative(String configuration) {
        return new Object[0];
    }

    @Override
    public Object[] findByIdNative(String configuration, int resourceId) {
        return new Object[0];
    }

    @Override
    public List<Configuration> findAllNative() {
        return null;
    }

    @Override
    public void persist(Configuration configuration) {

    }

    @Override
    public int insertNative(String configurationId, String sourceId, Date snapshot, String toolId, String configuration) {
        return getEntityManager()
                .createNamedQuery("Configuration.insertNative")
                .setParameter("conf_id", configurationId)
                .setParameter("source_id", sourceId)
                .setParameter("snapshot", snapshot)
                .setParameter("tool_id", toolId)
                .setParameter("configuration", configuration)
                .executeUpdate();
    }

    @Override
    public boolean deleteById(String configurationId) {
        return false;
    }

    @Override
    public void delete(Configuration configuration) {

    }

    @Override
    public Configuration update(Configuration configuration) {
        return null;
    }

    @Override
    public int updateByIdQuery(Configuration configuration) {
        return 0;
    }
}
