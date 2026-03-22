package jp.green_code.shared.dto;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public sealed interface Validated<T> permits Validated.Valid, Validated.Invalid {

    record Valid<T>(T value) implements Validated<T> {
        // thymeleaf で必要となるので空のerrors を返す
        public Map<String, ValidationStepResult> errors() {
            return Collections.emptyMap();
        }
    }

    record Invalid<T>(Map<String, ValidationStepResult> errors) implements Validated<T> {
        public Map<String, ValidationStepResult> toErrorMap() {
            return errors.entrySet().stream().filter(e -> !e.getValue().ok()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    static <T> Validated<T> toValidated(Map<String, ValidationStepResult> map, T value) {
        return map.values().stream().allMatch(ValidationStepResult::ok) ? new Valid<>(value) : new Invalid<>(map);
    }
}