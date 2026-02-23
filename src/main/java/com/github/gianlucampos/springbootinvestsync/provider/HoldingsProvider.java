package com.github.gianlucampos.springbootinvestsync.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gianlucampos.springbootinvestsync.exception.HoldingsProviderException;
import com.github.gianlucampos.springbootinvestsync.models.Ticker;
import com.github.gianlucampos.springbootinvestsync.models.TickerTypeEnum;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Component
public class HoldingsProvider {

    private final List<Ticker> tickers;

    public HoldingsProvider(String bucket, String objectKey) {
        try (S3Client s3 = S3Client.create()) {
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

            String jsonData = s3.getObjectAsBytes(request).asUtf8String();
            if (jsonData == null || jsonData.isBlank()) {
                throw new HoldingsProviderException("Datasource is empty");
            }
            ObjectMapper mapper = new ObjectMapper();
            tickers = Arrays.asList(mapper.readValue(jsonData, Ticker[].class));
        } catch (JsonProcessingException e) {
            throw new HoldingsProviderException("Error at gathering data from Datasource", e);
        }
    }

    public List<Ticker> getTickerByGroup(TickerTypeEnum tickerGroup) {
        return tickers.stream()
            .filter(t -> t.getTickerType() == tickerGroup)
            .toList();
    }
}
