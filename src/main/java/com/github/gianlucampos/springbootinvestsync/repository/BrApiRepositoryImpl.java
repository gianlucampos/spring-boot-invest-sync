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
import javax.naming.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BrApiRepositoryImpl implements BrApiRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String token;

    //TODO capar fora essa classe e injetar @Value no baseUrl e token
    public BrApiRepositoryImpl(ApiIntegrationsProperties props) {
        var brApi = props.getApis().get("br-api");
        this.baseUrl = brApi.getUrl();
        this.token = brApi.getToken();
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    public List<Ticker> getTickersFromList(List<String> tickersForSearch) {
        return tickersForSearch.parallelStream()
            .map(symbol -> {
                try {
                    String tokenQuery = "?token=".concat(token);
                    String formatedURL = baseUrl.concat(symbol).concat(tokenQuery);

                    HttpRequest request = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(formatedURL))
                        .build();

                    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                    if (response.statusCode() != 200) {
                        log.error("Error at calling BrApi: {}", response.body());
                        throw new ServiceUnavailableException("Error at calling BrApi: " + response.body());
                    }

                    JsonNode root = mapper.readTree(response.body());
                    JsonNode results = root.path("results");

                    return Ticker.builder()
                        .symbol(results.get(0).get("symbol").asText())
                        .marketPrice(results.get(0).get("regularMarketPrice").decimalValue())
                        .build();

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrupted while retrieving stocks", ex);
                    throw new StockApiException(ex);

                } catch (Exception ex) {
                    log.error("Error at retrieving stocks", ex);
                    throw new StockApiException(ex);
                }
            }).toList();
    }
}
