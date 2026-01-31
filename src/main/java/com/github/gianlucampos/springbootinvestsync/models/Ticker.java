package com.github.gianlucampos.springbootinvestsync.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Ticker {

    private String symbol;
    private BigDecimal value;
    private Double quantity;

}
