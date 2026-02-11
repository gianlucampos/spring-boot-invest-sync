package com.github.gianlucampos.springbootinvestsync.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gianlucampos.springbootinvestsync.config.ApiIntegrationsProperties;
import com.github.gianlucampos.springbootinvestsync.exception.StockApiException;
import com.github.gianlucampos.springbootinvestsync.models.Ticker;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BrApiRepositoryImpl implements BrApiRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String token;

    public BrApiRepositoryImpl(ApiIntegrationsProperties props) {
        log.info("APIs carregadas: {}", props.getApis());
        var brApi = props.getApis().get("br-api");
        this.baseUrl = brApi.getUrl();
        this.token = brApi.getToken();
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    public List<Ticker> getTickersFromList(List<String> tickersForSearch) {
        try {
            String symbols = String.join(",", tickersForSearch);
            String tokenQuery = "?token=".concat(token);
            String formatedURL = baseUrl.concat(symbols).concat(tokenQuery);

            HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(formatedURL))
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            JsonNode results = root.path("results");

            var listOfTickers = StreamSupport.stream(results.spliterator(), false)
                .map(item -> Ticker.builder()
                    .symbol(item.get("symbol").asText())
                    .value(item.get("regularMarketPrice").decimalValue())
                    .build()
                ).toList();

            log.info("\nList of tickers:\n{}", listOfTickers.stream()
                .map(Ticker::toString)
                .collect(Collectors.joining("\n"))
            );

            return listOfTickers;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while retrieving stocks", ex);
            throw new StockApiException(ex);

        } catch (Exception ex) {
            log.error("Error at retrieving stocks", ex);
            throw new StockApiException(ex);
        }
    }
}
