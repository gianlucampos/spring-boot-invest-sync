package com.github.gianlucampos.springbootinvestsync.config;

import com.github.gianlucampos.springbootinvestsync.exception.SheetsException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SheetsFactory {

    private static final String APPLICATION_NAME = "Sheet-GoogleAPI";

    //TODO Pegar do bucket ao invés de deixar exposto
    @Value("${google.sheets.credentials}")
    private String credentialsJson;

    public Sheets createSheetsService() {
        try {
            GoogleCredentials credentials = loadCredentials();

            return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
            )
                .setApplicationName(APPLICATION_NAME)
                .build();
        } catch (Exception e) {
            throw new SheetsException("Failed to create Sheets client", e);
        }
    }

    private GoogleCredentials loadCredentials() throws IOException {
        if (!StringUtils.hasText(credentialsJson)) {
            throw new SheetsException("Google Sheets credentials not set");
        }
        try (var inputStream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))) {
            return GoogleCredentials.fromStream(inputStream).createScoped(SheetsScopes.SPREADSHEETS);
        }
    }
}
