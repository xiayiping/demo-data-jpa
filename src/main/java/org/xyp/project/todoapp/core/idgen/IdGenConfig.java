package org.xyp.project.todoapp.core.idgen;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.id-gen")
@Data
public class IdGenConfig {
    long defaultBatchSize = 10;
    long defaultIdStartsFrom = 100000;
}
