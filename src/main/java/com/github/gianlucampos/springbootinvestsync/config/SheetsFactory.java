package com.github.gianlucampos.springbootinvestsync.config;

import com.github.gianlucampos.springbootinvestsync.exception.SheetsException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SheetsFactory {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Sheet-GoogleAPI";

    @Value("${google.sheets.credentials-file}")
    private String credentialsFilePath;

    public Sheets createSheetsService() throws GeneralSecurityException, IOException {
        InputStream in = SheetsFactory.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new SheetsException("Credentials not found: " + credentialsFilePath);
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
            .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        return new Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JSON_FACTORY,
            new HttpCredentialsAdapter(credentials)
        )
            .setApplicationName(APPLICATION_NAME)
            .build();
    }
}
