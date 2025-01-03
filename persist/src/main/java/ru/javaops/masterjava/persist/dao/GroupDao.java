package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao {

    @Override
    @SqlUpdate("TRUNCATE groups")
    public abstract void clean();

    @SqlUpdate("INSERT INTO groups (id, user_id, project_name, name, type) VALUES (:id, :userId, projectName, :name, :type) ")
    public abstract void insert(@BindBean Group group);

    @SqlQuery("SELECT * FROM groups ORDER BY name LIMIT :it")
    public abstract List<Group> getWithLimit(@Bind int limit);
}
