package de.serverfrog.frogchat;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author bastian.venz
 */
@Getter
@ToString
@AllArgsConstructor
@Stateful
public class ApplicationConfig implements Serializable {

    private final String password;


    public ApplicationConfig() {
        password = generateConfig().password;
    }
    
//    
//    @Produces
    private static ApplicationConfig generateConfig() {
        System.out.println("Generate?");
        Properties prop = new Properties();
        try {
            prop.load(ApplicationConfig.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to load properties.", ex);
        }
        ApplicationConfig config = new ApplicationConfig(prop.getProperty("password"));
        return config;
    }
}
