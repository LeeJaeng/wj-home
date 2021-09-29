package org.woojeong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WjApplication {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml,"
            + "classpath:database.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(WjApplication.class).properties(APPLICATION_LOCATIONS).run(args);
//        SpringApplication.run(WjApplication.class, args);
    }

}