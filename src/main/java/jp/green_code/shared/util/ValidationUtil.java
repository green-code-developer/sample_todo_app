package jp.green_code.shared.util;

import jp.green_code.shared.dto.MessageKey;
import jp.green_code.shared.dto.ValidationError;
import jp.green_code.shared.dto.ValidationStepResult;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static jp.green_code.shared.dto.ValidationStepResult.OK_BUT_STOP_RESULT;
import static jp.green_code.shared.dto.ValidationStepResult.OK_RESULT;
import static jp.green_code.shared.dto.ValidationStepResult.ValidationFlow.FAIL_TO_STOP;
import static jp.green_code.shared.dto.ValidationStepResult.ValidationFlow.OK;
import static jp.green_code.shared.dto.ValidationStepResult.failButContinue;
import static org.slf4j.LoggerFactory.getLogger;

public class ValidationUtil {
    private static final Logger log = getLogger(ValidationUtil.class);

    public static final String REQUIRED = "validation.required";
    public static final String GREATER_THAN = "validation.greater_than";
    public static final String GREATER_THAN_EQUALS = "validation.greater_than_equals";
    public static final String LESS_THAN = "validation.less_than";
    public static final String LESS_THAN_EQUALS = "validation.less_than_equals";
    public static final String DATE_TIME_FORMAT = "validation.date_time_format";
    public static final String TEXT_MIN_LENGTH = "validation.text_min_length";
    public static final String TEXT_MAX_LENGTH = "validation.text_max_length";
    public static final String INVALID_ENUM = "validation.invalid_enum";

    public interface Validator<T> {
        ValidationStepResult validate(T value);
    }

    public static <T> ValidationStepResult validateAllowNull(@Nullable T value, List<Validator<T>> validators) {
        if (value == null) {
            return new ValidationStepResult(OK, List.of());
        }
        return execute(value, validators);
    }

    public static <T> ValidationStepResult validateAllowNull(@Nullable T value, Validator<T> validator) {
        return validateAllowNull(value, List.of(validator));
    }

    public static <T> ValidationStepResult validateRequired(@Nullable T value, List<Validator<T>> validators) {
        if (value == null) {
            return new ValidationStepResult(FAIL_TO_STOP, List.of(new ValidationError(new MessageKey(REQUIRED))));
        }
        return execute(value, validators);
    }

    public static <T> ValidationStepResult validateRequired(@Nullable T value, Validator<T> validator) {
        return validateRequired(value, List.of(validator));
    }

    private static <T> ValidationStepResult execute(T value, List<Validator<T>> validators) {
        var lastFlow = OK;
        var errors = new ArrayList<ValidationError>();
        for (var validator : validators) {
            var result = validator.validate(value);
            if (lastFlow.getSeverity() <= result.flow().getSeverity()) {
                lastFlow = result.flow();
            }
            if (!result.flow().isOk()) {
                errors.addAll(result.errors());
            }
            if (result.flow().isStop()) {
                break;
            }
        }
        return new ValidationStepResult(lastFlow, errors);
    }

    public static <T extends Comparable<T>> Validator<T> min(T threshold, boolean inclusive) {
        return (value) -> {
            var cmp = value.compareTo(threshold);
            var valid = inclusive ? 0 <= cmp : 0 < cmp;
            if (valid) {
                return OK_RESULT;
            }
            var key = inclusive ? GREATER_THAN_EQUALS : GREATER_THAN;
            return failButContinue(new ValidationError(new MessageKey(key, threshold)));
        };
    }

    public static <T extends Comparable<T>> Validator<T> max(T threshold, boolean inclusive) {
        return (value) -> {
            var cmp = value.compareTo(threshold);
            var valid = inclusive ? cmp <= 0 : cmp < 0;
            if (valid) {
                return OK_RESULT;
            }
            var key = inclusive ? LESS_THAN_EQUALS : LESS_THAN;
            return failButContinue(new ValidationError(new MessageKey(key, threshold)));
        };
    }

    public static <T extends CharSequence> Validator<T> offsetDateTimeTextValidator(String pattern, @Nullable Consumer<OffsetDateTime> consumer) {
        var formatter = DateTimeFormatter.ofPattern(pattern);
        return (value) -> {
            try {
                var data = OffsetDateTime.parse(value, formatter);
                if (consumer != null) {
                    consumer.accept(data);
                }
                return OK_RESULT;
            } catch (DateTimeParseException e) {
                log.debug("Failed to parse value:{}, pattern:{}", value, pattern, e);
                return ValidationStepResult.failToStop(new ValidationError(new MessageKey(DATE_TIME_FORMAT, pattern)));
            }
        };
    }

    public static <T extends CharSequence> Validator<T> localDateTimeTextValidator(String pattern, @Nullable Consumer<LocalDateTime> consumer) {
        var formatter = DateTimeFormatter.ofPattern(pattern);
        return (value) -> {
            try {
                var data = LocalDateTime.parse(value, formatter);
                if (consumer != null) {
                    consumer.accept(data);
                }
                return OK_RESULT;
            } catch (DateTimeParseException e) {
                log.debug("Failed to parse value:{}, pattern:{}", value, pattern, e);
                return ValidationStepResult.failToStop(new ValidationError(new MessageKey(DATE_TIME_FORMAT, pattern)));
            }
        };
    }

    public static <T extends CharSequence> Validator<T> localDateTextValidator(String pattern, @Nullable Consumer<LocalDate> consumer) {
        var formatter = DateTimeFormatter.ofPattern(pattern);
        return (value) -> {
            try {
                var data = LocalDate.parse(value, formatter);
                if (consumer != null) {
                    consumer.accept(data);
                }
                return OK_RESULT;
            } catch (DateTimeParseException e) {
                return ValidationStepResult.failToStop(new ValidationError(new MessageKey(DATE_TIME_FORMAT, pattern)));
            }
        };
    }

    public static Validator<CharSequence> textMinLength(int threshold) {
        return (value) -> {
            if (threshold <= value.length()) {
                return OK_RESULT;
            }
            return failButContinue(new ValidationError(new MessageKey(TEXT_MIN_LENGTH, threshold)));
        };
    }

    public static Validator<CharSequence> textMaxLength(int threshold) {
        return (value) -> {
            if (value.length() <= threshold) {
                return OK_RESULT;
            }
            return failButContinue(new ValidationError(new MessageKey(TEXT_MAX_LENGTH, threshold)));
        };
    }

    public static <T extends Enum<T>> Validator<CharSequence> enumValidator(Class<T> clazz, @Nullable Consumer<T> consumer) {
        return (value) -> {
            try {
                T result = Enum.valueOf(clazz, value.toString());
                if (consumer != null) {
                    consumer.accept(result);
                }
                return OK_RESULT;
            } catch (IllegalArgumentException e) {
                return ValidationStepResult.failToStop(new ValidationError(new MessageKey(INVALID_ENUM)));
            }
        };
    }

    public static Validator<CharSequence> stopIfEmpty() {
        return (value) -> {
            if (value.isEmpty()) {
                return OK_BUT_STOP_RESULT;
            } else {
                return OK_RESULT;
            }
        };
    }
}
