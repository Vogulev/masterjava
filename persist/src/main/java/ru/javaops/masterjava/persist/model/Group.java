package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Group extends BaseEntity {
    private @NonNull String name;
    @Column("user_id")
    private @NonNull Integer userId;
    @Column("project_name")
    private @NonNull String projectName;
    private @NonNull GroupType type;
}
