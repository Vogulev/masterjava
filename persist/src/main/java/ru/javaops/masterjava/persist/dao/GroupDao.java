package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao {

    @Override
    @SqlUpdate("TRUNCATE groups")
    public abstract void clean();

    public Group insert(Group group) {
        if (group.isNew()) {
            int id = insertGeneratedId(group);
            group.setId(id);
        } else {
            insertWitId(group);
        }
        return group;
    }

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO groups (user_id, project_name, name, type) VALUES (:userId, :projectName, :name, CAST(:type AS GROUP_TYPE))")
    abstract int insertGeneratedId(@BindBean Group group);

    @SqlUpdate("INSERT INTO users (id, user_id, project_name, name, type) VALUES (:id, :userId, :projectName, :name, CAST(:type AS GROUP_TYPE))")
    abstract void insertWitId(@BindBean Group group);

    @SqlQuery("SELECT * FROM groups WHERE project_name = :project")
    public abstract List<Group> findAllByProject(@Bind("project") String projectName);
}
