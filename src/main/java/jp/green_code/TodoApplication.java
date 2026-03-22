package jp.green_code;

import jp.green_code.todo.domain.config.AppProperties;
import jp.green_code.shared.config.AppSharedProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
// @ConfigurationProperties を使うクラスを指定する必要がある
@EnableConfigurationProperties({AppSharedProperties.class, AppProperties.class})
public class TodoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }
}
