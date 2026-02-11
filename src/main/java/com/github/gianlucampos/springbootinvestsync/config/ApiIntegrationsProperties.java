package com.github.gianlucampos.springbootinvestsync.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integrations")
public class ApiIntegrationsProperties {

    private Map<String, ApiProps> apis = new HashMap<>();

    @Getter
    @Setter
    public static class ApiProps {
        private String url;
        private String token;
    }

    public ApiProps get(String key) {
        return apis.get(key);
    }
}

