package jp.green_code.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
public class TimeConfig {

    private final AppSharedProperties appSharedProperties;

    public TimeConfig(AppSharedProperties appSharedProperties) {
        this.appSharedProperties = appSharedProperties;
    }

    @Bean
    public ZoneId zoneId() {
        return ZoneId.of(appSharedProperties.getZoneId());
    }
}
