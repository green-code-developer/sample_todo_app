package jp.green_code.shared.config;

import org.jspecify.annotations.NullUnmarked;
import org.springframework.boot.context.properties.ConfigurationProperties;

// Main クラスにこの設定が必要
// @EnableConfigurationProperties({AppSharedProperties.class, AppProperties.class})
@NullUnmarked
@ConfigurationProperties(prefix = "app-shared")
public class AppSharedProperties {
    private String zoneId;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
