package com.github.gianlucampos.springbootinvestsync.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gianlucampos.springbootinvestsync.config.ApiIntegrationsProperties;
import com.github.gianlucampos.springbootinvestsync.exception.StockApiException;
import com.github.gianlucampos.springbootinvestsync.models.Ticker;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import javax.naming.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UsaApiRepositoryImpl implements UsaApiRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String token;

    public UsaApiRepositoryImpl(ApiIntegrationsProperties props) {
        var api = props.getApis().get("usa-api");
        this.baseUrl = api.getUrl();
        this.token = api.getToken();
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    public List<Ticker> getTickersFromList(List<String> tickersForSearch) {
        return tickersForSearch.parallelStream()
            .map(symbol -> {
                try {
                    String symbolQuery = "?symbol=".concat(symbol);
                    String tokenQuery = "&token=".concat(token);
                    String formatedURL = baseUrl.concat(symbolQuery).concat(tokenQuery);

                    var request = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(formatedURL))
                        .build();

                    var response = client.send(request, BodyHandlers.ofString());
                    if (response.statusCode() != 200) {
                        log.error("Error at calling UsaApi: {}", response.body());
                        throw new ServiceUnavailableException("Error at calling UsaApi: " + response.body());
                    }
                    var root = mapper.readTree(response.body());

                    return Ticker.builder()
                        .symbol(symbol)
                        .marketPrice(BigDecimal.valueOf(root.get("c").asDouble()))
                        .build();

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrupted while retrieving stocks", ex);
                    throw new StockApiException(ex);

                } catch (Exception ex) {
                    log.error("Error retrieving stocks: {}", symbol, ex);
                    throw new StockApiException(ex);
                }
            })
            .toList();
    }
}
