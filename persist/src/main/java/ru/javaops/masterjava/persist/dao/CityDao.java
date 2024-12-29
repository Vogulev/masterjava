package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    @Override
    @SqlUpdate("TRUNCATE cities")
    public abstract void clean();

    @SqlUpdate("INSERT INTO cities (id, name) VALUES (:id, :name) ")
    public abstract void insert(@BindBean City city);
}
