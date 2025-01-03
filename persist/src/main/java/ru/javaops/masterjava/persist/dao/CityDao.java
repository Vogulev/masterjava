package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    @Override
    @SqlUpdate("TRUNCATE cities CASCADE")
    public abstract void clean();

    @SqlUpdate("INSERT INTO cities (id, name) VALUES (:id, :name) ON CONFLICT DO NOTHING")
    public abstract void insert(@BindBean City city);

    @SqlQuery("SELECT * FROM cities ORDER BY name LIMIT :it")
    public abstract List<City> getWithLimit(@Bind int limit);
}
