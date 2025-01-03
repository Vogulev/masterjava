package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Group extends BaseEntity {
    private @NonNull String name;
    private @NonNull Integer userId;
    private @NonNull String projectName;
    private @NonNull GroupType type;
}
