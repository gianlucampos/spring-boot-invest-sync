package com.github.gianlucampos.springbootinvestsync.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"buyPrice", "quantity", "tickerType"})
public class Ticker {

    private String symbol;
    private BigDecimal marketPrice;
    private BigDecimal buyPrice;
    private Double quantity;
    private TickerTypeEnum tickerType;

    public BigDecimal getTotalPrice() {
        return marketPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getAveragePrice() {
        return buyPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
