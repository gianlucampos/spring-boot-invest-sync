package com.github.gianlucampos.springbootinvestsync.config;

import com.github.gianlucampos.springbootinvestsync.provider.HoldingsProvider;
import com.google.api.services.sheets.v4.Sheets;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiIntegrationsProperties.class)
public class AppConfig {

    @Bean
    public Sheets sheets(SheetsFactory sheetsFactory) {
        return sheetsFactory.createSheetsService();
    }

    @Bean
    public HoldingsProvider holdingsProvider() {
        return new HoldingsProvider(System.getenv("S3_BUCKET"), System.getenv("S3_OBJECT_KEY"));
    }

}
