package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Group;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao {

    @Override
    @SqlUpdate("TRUNCATE groups")
    public abstract void clean();

    @SqlUpdate("INSERT INTO groups (id, user_id, project_name, name, type) VALUES (:id, :userId, projectName, :name, :type) ")
    abstract void insert(@BindBean Group group);
}
