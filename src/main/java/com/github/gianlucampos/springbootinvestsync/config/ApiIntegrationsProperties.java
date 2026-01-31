package com.github.gianlucampos.springbootinvestsync.config;

import com.github.gianlucampos.springbootinvestsync.config.ApiIntegrationsProperties.ApiProps;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "integrations")
public class ApiIntegrationsProperties extends HashMap<String, ApiProps> {

    @Getter
    @Setter
    public static class ApiProps {
        private String url;
        private String token;
    }
}

