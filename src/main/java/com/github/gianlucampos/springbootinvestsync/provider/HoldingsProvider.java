package com.github.gianlucampos.springbootinvestsync.provider;

import com.github.gianlucampos.springbootinvestsync.models.Ticker;
import java.util.List;

public class HoldingsProvider {

    public static List<Ticker> fiis() {
        return List.of(
            new Ticker("FIIB11", null, 11.0),
            new Ticker("HGLG11", null, 45.0),
            new Ticker("IRIM11", null, 238.0),
            new Ticker("MXRF11", null, 665.0),
            new Ticker("PVBI11", null, 63.0),
            new Ticker("VISC11", null, 46.0),
            new Ticker("XPLG11", null, 52.0)
        );
    }

    public static List<Ticker> stocks() {
        return List.of(
            new Ticker("WEGE3", null, 267.0),
            new Ticker("TAEE3", null, 570.0),
            new Ticker("VALE3", null, 95.0),
            new Ticker("ABEV3", null, 535.0),
            new Ticker("B3SA3", null, 330.0),
            new Ticker("ITSA4", null, 415.0),
            new Ticker("PSSA3", null, 110.0),
            new Ticker("QUAL3", null, 100.0)
        );
    }

    public static List<Ticker> reits() {
        return List.of(
            new Ticker("GOOD", null, 138.74188827),
            new Ticker("STAG", null, 26.79011551),
            new Ticker("AVB", null, 4.48415412),
            new Ticker("EQIX", null, 0.21650201),
            new Ticker("AGNC", null, 41.95688748)
        );
    }
}
