package ru.javaops.masterjava.persist;

import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.List;

public class GroupTestData {
    public static Group TOPJAVA_6;
    public static Group TOPJAVA_7;
    public static Group TOPJAVA_8;
    public static List<Group> GROUPS;


    public static void init() {
        TOPJAVA_6 = new Group("topjava06", 10000, "project", GroupType.FINISHED);
        TOPJAVA_7 = new Group("topjava07", 10000, "project", GroupType.FINISHED);
        TOPJAVA_8 = new Group("topjava08", 10000, "project", GroupType.CURRENT);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            GROUPS.forEach(dao::insert);
        });
    }
}
