package com.github.gianlucampos.springbootinvestsync.config;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void initFallbacks() {
        ApiProps brApi = apis.get("br-api");
        if (brApi == null) {
            brApi = new ApiProps();
            brApi.setUrl(System.getenv("BR_API_URL"));
            brApi.setToken(System.getenv("BR_API_TOKEN"));
            apis.put("br-api", brApi);
        }

        ApiProps usaApi = apis.get("usa-api");
        if (usaApi == null) {
            usaApi = new ApiProps();
            usaApi.setUrl(System.getenv("USA_API_URL"));
            apis.put("usa-api", usaApi);
        }
    }
}
