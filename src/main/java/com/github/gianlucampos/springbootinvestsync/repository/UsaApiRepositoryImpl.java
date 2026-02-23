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
import java.util.stream.Collectors;
import javax.naming.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UsaApiRepositoryImpl implements UsaApiRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;


    public UsaApiRepositoryImpl(ApiIntegrationsProperties props) {
        var brApi = props.getApis().get("usa-api");
        this.baseUrl = brApi.getUrl();
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    public List<Ticker> getTickersFromList(List<String> tickersForSearch) {
        var listOfTickers = tickersForSearch.parallelStream()
            .map(symbol -> {
                try {
                    var url = baseUrl.concat(symbol);
                    var request = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(url))
                        .build();

                    var response = client.send(request, BodyHandlers.ofString());
                    if (response.statusCode() != 200) {
                        log.error("Error at calling UsaApi: {}", response.body());
                        throw new ServiceUnavailableException("Error at calling UsaApi: " + response.body());
                    }
                    var root = mapper.readTree(response.body());

                    return Ticker.builder()
                        .symbol(root.get("Ticker").asText())
                        .marketPrice(BigDecimal.valueOf(root.get("Price").asDouble()))
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

        log.info("\nList of tickers:\n{}", listOfTickers.stream()
            .map(Ticker::toString)
            .collect(Collectors.joining("\n"))
        );
        return listOfTickers;
    }
}
