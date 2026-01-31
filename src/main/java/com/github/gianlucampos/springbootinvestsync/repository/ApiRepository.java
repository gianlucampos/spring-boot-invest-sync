package com.github.gianlucampos.springbootinvestsync.repository;

import com.github.gianlucampos.springbootinvestsync.models.Ticker;
import java.util.List;

public interface ApiRepository {

    List<Ticker> getTickersFromList(List<String> tickersForSearch);

}
