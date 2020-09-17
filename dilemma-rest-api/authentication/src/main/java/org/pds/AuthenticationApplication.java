package org.pds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;
import java.util.TimeZone;

@SpringBootApplication
public class AuthenticationApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
        Properties props = new Properties();
        props.put("server.ssl.key-store", args[0]);
        props.put("server.ssl.key-store-password", args[1]);
        props.put("server.ssl.key-store-type", args[2]);
        props.put("server.ssl.key-alias", args[3]);
        props.put("sever.aes.key-password", args[4]);

        new SpringApplicationBuilder(AuthenticationApplication.class)
                .properties(props).run(args);
    }
}
