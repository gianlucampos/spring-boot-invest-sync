package com.github.gianlucampos.springbootinvestsync.startup;

import com.github.gianlucampos.springbootinvestsync.service.PortfolioSyncService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class StartupRunner implements CommandLineRunner {

    private final PortfolioSyncService portfolioSyncService;

    @Override
    public void run(String... args) {
        portfolioSyncService.updateAll();
    }
}
