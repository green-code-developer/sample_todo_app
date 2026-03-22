package jp.green_code.shared.component;

import jp.green_code.shared.dto.Validated;
import org.springframework.stereotype.Component;

@Component
public class LogFormatter {

    private final JsonComponent jsonComponent;

    public LogFormatter(JsonComponent jsonComponent) {
        this.jsonComponent = jsonComponent;
    }

    public String start(String method, String msg) {
        return "START %s %s".formatted(method, msg);
    }

    public String end(String method, String msg) {
        return "END %s %s".formatted(method, msg);
    }

    public String error(String method, String msg) {
        return "ERROR %s %s".formatted(method, msg);
    }

    public String validationError(String method, Validated.Invalid<?> invalid) {
        var message = jsonComponent.toJson(invalid.toErrorMap());
        return "ERROR %s validation error %s".formatted(method, message);
    }
}
