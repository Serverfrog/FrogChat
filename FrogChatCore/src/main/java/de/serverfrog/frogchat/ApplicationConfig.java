package de.serverfrog.frogchat;

import java.io.IOException;
import java.util.Properties;

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
public class ApplicationConfig {

    
    private final String password;
    
    @Produces
    private ApplicationConfig generateConfig() {
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
