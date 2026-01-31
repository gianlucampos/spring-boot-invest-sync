package com.github.gianlucampos.springbootinvestsync.config;

import com.google.api.services.sheets.v4.Sheets;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Sheets sheets(SheetsFactory sheetsFactory) throws GeneralSecurityException, IOException {
        return sheetsFactory.createSheetsService();
    }
}
