package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.service.mail.model.MailModel;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailDao implements AbstractDao {

    @SqlUpdate("INSERT INTO mails (success, receiver, cc, subject, body, result) VALUES (:success, :to, :cc, :subject, :body, :result)")
    @GetGeneratedKeys
    public abstract int saveMailResult(@BindBean MailModel mailModel);
}
