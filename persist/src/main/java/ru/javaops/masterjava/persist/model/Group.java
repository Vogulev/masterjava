package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Group {
    private String id;
    private String name;
    private String userId;
    private String projectName;
    private GroupType type;
}
