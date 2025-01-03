package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class City {
    private @NonNull String id;
    private @NonNull String name;
}
