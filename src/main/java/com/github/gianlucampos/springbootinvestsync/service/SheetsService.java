package com.github.gianlucampos.springbootinvestsync.service;

import com.github.gianlucampos.springbootinvestsync.exception.SheetsException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class SheetsService {

    public static final String MAJOR_DIMENSION = "COLUMNS";
    public static final String VALUE_INPUT_OPTION = "USER_ENTERED";
    private static final String SPREADSHEET_ID = "18mN_cZpjXXc5ubbiZVocn_40a07kWEXsWRPcm1l91sc";

    private final Sheets sheets;

    public void updateSheet(String content, String range) {
        try {
            ValueRange body = new ValueRange();
            List<Object> values = new ArrayList<>();
            values.add(content);
            body.setValues(Collections.singletonList(values));
            body.setMajorDimension(MAJOR_DIMENSION);
            sheets.spreadsheets().values()
                .update(SPREADSHEET_ID, range, body)
                .setValueInputOption(VALUE_INPUT_OPTION)
                .execute();
            log.info("Successful update!");
        } catch (Exception e) {
            log.error("Failed update!", e);
            throw new SheetsException(e);
        }
    }

}
