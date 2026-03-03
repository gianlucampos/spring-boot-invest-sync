package com.github.gianlucampos.springbootinvestsync.service;

import static com.github.gianlucampos.springbootinvestsync.utils.UtilConstants.FIIS_RANGE;
import static com.github.gianlucampos.springbootinvestsync.utils.UtilConstants.REITS_RANGE;
import static com.github.gianlucampos.springbootinvestsync.utils.UtilConstants.STOCKS_RANGE;

import com.github.gianlucampos.springbootinvestsync.models.Ticker;
import com.github.gianlucampos.springbootinvestsync.models.TickerTypeEnum;
import com.github.gianlucampos.springbootinvestsync.provider.HoldingsProvider;
import com.github.gianlucampos.springbootinvestsync.repository.BrApiRepository;
import com.github.gianlucampos.springbootinvestsync.repository.UsaApiRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PortfolioSyncService {

    private final SheetsService sheetsService;
    private final BrApiRepository brApiRepository;
    private final UsaApiRepository usaApiRepository;
    private final HoldingsProvider holdingsProvider;

    public void updateAll() {
        updateHoldings(holdingsProvider.getTickerByGroup(TickerTypeEnum.FII), FIIS_RANGE);
        updateHoldings(holdingsProvider.getTickerByGroup(TickerTypeEnum.STOCK), STOCKS_RANGE);
        updateHoldings(holdingsProvider.getTickerByGroup(TickerTypeEnum.REIT), REITS_RANGE);
    }

    public void updateHoldings(List<Ticker> myHoldings, String holdingsRange) {
        var apiRepository = switch (holdingsRange) {
            case FIIS_RANGE, STOCKS_RANGE -> brApiRepository;
            case REITS_RANGE -> usaApiRepository;
            default -> throw new IllegalArgumentException("Unkown range: " + holdingsRange);
        };

        List<String> symbols = myHoldings.stream()
            .map(Ticker::getSymbol)
            .toList();

        Map<String, BigDecimal> pricesBySymbol = apiRepository.getTickersFromList(symbols)
            .stream()
            .collect(Collectors.toMap(
                Ticker::getSymbol,
                Ticker::getMarketPrice
            ));

        BigDecimal total = myHoldings.stream()
            .map(holding -> pricesBySymbol.get(holding.getSymbol())
                .multiply(BigDecimal.valueOf(holding.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        sheetsService.updateSheet(total.toString(), holdingsRange);
    }

}
