package com.retail.mortgage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.retail.mortgage.constants.MortgageConstants;

@SpringBootApplication
public class MortgageApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MortgageApplication.class);
        app.setAdditionalProfiles(getActiveProfile(args));
        app.run(args);
    }

    /**
     * Method to decide active profile, default is kept as local
     */
    private static String getActiveProfile(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--spring.profiles.active=")) {
                return arg.substring("--spring.profiles.active=".length());
            }
        }
        return MortgageConstants.DEFAULT_PROFILE;
    }
}
