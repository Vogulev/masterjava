package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.List;

public class GroupTestData {
    public static Group TOPJAVA_6;
    public static Group TOPJAVA_7;
    public static Group TOPJAVA_8;
    public static Group MASTERJAVA_6;
    public static List<Group> GROUPS;

    public static void init() {
        TOPJAVA_6 = new Group("topjava06", 100001, "topjava", GroupType.FINISHED);
        TOPJAVA_7 = new Group("topjava07", 100001, "topjava", GroupType.FINISHED);
        TOPJAVA_8 = new Group("topjava08", 100002, "topjava", GroupType.CURRENT);
        MASTERJAVA_6 = new Group("masterjava6", 100002, "masterjava", GroupType.CURRENT);
        GROUPS = ImmutableList.of(TOPJAVA_6, TOPJAVA_7, TOPJAVA_8);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            GROUPS.forEach(dao::insert);
            dao.insert(MASTERJAVA_6);
        });
    }
}
