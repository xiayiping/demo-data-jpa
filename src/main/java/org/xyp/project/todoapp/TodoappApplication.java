package org.xyp.project.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.xyp.project.todoapp.core.idgen.IdGenConfig;

@EnableConfigurationProperties({
  IdGenConfig.class,
})
@SpringBootApplication
public class TodoappApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoappApplication.class, args);
    }

}
