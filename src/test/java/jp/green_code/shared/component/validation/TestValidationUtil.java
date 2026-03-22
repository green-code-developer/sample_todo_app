package jp.green_code.shared.component.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static jp.green_code.shared.util.ValidationUtil.DATE_TIME_FORMAT;
import static jp.green_code.shared.util.ValidationUtil.GREATER_THAN;
import static jp.green_code.shared.util.ValidationUtil.GREATER_THAN_EQUALS;
import static jp.green_code.shared.util.ValidationUtil.LESS_THAN;
import static jp.green_code.shared.util.ValidationUtil.LESS_THAN_EQUALS;
import static jp.green_code.shared.util.ValidationUtil.max;
import static jp.green_code.shared.util.ValidationUtil.min;
import static jp.green_code.shared.util.ValidationUtil.offsetDateTimeTextValidator;
import static jp.green_code.shared.util.ValidationUtil.validateAllowNull;
import static jp.green_code.shared.util.ValidationUtil.validateRequired;
import static jp.green_code.shared.dto.ValidationStepResult.ValidationFlow.FAIL_BUT_CONTINUE;
import static jp.green_code.shared.dto.ValidationStepResult.ValidationFlow.FAIL_TO_STOP;
import static jp.green_code.shared.dto.ValidationStepResult.ValidationFlow.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestValidationUtil {

    @Test
    @DisplayName("validateAllowNull() null はOK")
    void testValidateAllowNull() {
        Integer nullInt = null;
        var result = validateAllowNull(nullInt, List.of(min(0, false)));
        assertEquals(OK, result.flow());
    }

    @Test
    @DisplayName("validateRequired() null はエラ")
    void testValidateRequired() {
        Integer nullInt = null;
        var result = validateRequired(nullInt, List.of(min(0, false)));
        assertEquals(FAIL_TO_STOP, result.flow());
    }

    @Test
    @DisplayName("整数の最小値バリデーション")
    void testIntMin() {
        // 1 以上
        var result = validateAllowNull(0, min(1, true));
        assertEquals(FAIL_BUT_CONTINUE, result.flow());
        assertEquals(GREATER_THAN_EQUALS, result.errors().getFirst().messageKey().key());
        assertEquals(1, result.errors().getFirst().messageKey().args()[0]);

        result = validateAllowNull(1, min(1, true));
        assertEquals(OK, result.flow());

        // 1 より大きい
        result = validateAllowNull(1, min(1, false));
        assertEquals(FAIL_BUT_CONTINUE, result.flow());
        assertEquals(GREATER_THAN, result.errors().getFirst().messageKey().key());
        assertEquals(1, result.errors().getFirst().messageKey().args()[0]);

        result = validateAllowNull(2, min(1, false));
        assertEquals(OK, result.flow());
    }

    @Test
    @DisplayName("整数の最大値バリデーション")
    void testIntMax() {
        // 100 以下
        var result = validateAllowNull(101, max(100, true));
        assertEquals(FAIL_BUT_CONTINUE, result.flow());
        assertEquals(LESS_THAN_EQUALS, result.errors().getFirst().messageKey().key());
        assertEquals(100, result.errors().getFirst().messageKey().args()[0]);

        result = validateAllowNull(100, max(100, true));
        assertEquals(OK, result.flow());

        // 100 より小さい
        result = validateAllowNull(100, max(100, false));
        assertEquals(FAIL_BUT_CONTINUE, result.flow());
        assertEquals(LESS_THAN, result.errors().getFirst().messageKey().key());
        assertEquals(100, result.errors().getFirst().messageKey().args()[0]);

        result = validateAllowNull(99, max(100, false));
        assertEquals(OK, result.flow());
    }

    @Test
    @DisplayName("日付の最小値バリデーション。整数と同じmin() を使っている。max() は省略")
    void testOffsetDateTimeMin() {
        var threshold = LocalDateTime.of(2000, 1, 1, 1, 1, 1);

        // 日時 閾値含む
        var result = validateAllowNull(threshold.minusSeconds(1), min(threshold, true));
        assertEquals(FAIL_BUT_CONTINUE, result.flow());
        assertEquals(GREATER_THAN_EQUALS, result.errors().getFirst().messageKey().key());
        assertEquals(threshold, result.errors().getFirst().messageKey().args()[0]);

        result = validateAllowNull(threshold, min(threshold, true));
        assertEquals(OK, result.flow());

        // 日時 閾値含まない
        result = validateAllowNull(threshold, min(threshold, false));
        assertEquals(FAIL_BUT_CONTINUE, result.flow());
        assertEquals(GREATER_THAN, result.errors().getFirst().messageKey().key());
        assertEquals(threshold, result.errors().getFirst().messageKey().args()[0]);

        result = validateAllowNull(threshold.plusSeconds(1), min(threshold, false));
        assertEquals(OK, result.flow());
    }

    @Test
    @DisplayName("offsetDateTimeTextValidator() consumer の確認含む")
    void testOffsetDateTimeMax() {
        // 正常系 consumer 経由でインスタンスを取得できる
        var timeRef = new AtomicReference<OffsetDateTime>();
        Consumer<OffsetDateTime> consumer = timeRef::set;
        var pattern = "y-M-d H:m:sZ";
        var result = validateAllowNull("2026-03-16 10:15:30+0900", offsetDateTimeTextValidator(pattern, consumer));
        assertEquals(OK, result.flow());
        var expected = OffsetDateTime.parse("2026-03-16T10:15:30+09:00");
        assertEquals(expected, timeRef.get());

        // 異常系 フォーマットエラー
        result = validateAllowNull("@026-03-16 10:15:30+0900", offsetDateTimeTextValidator(pattern, consumer));
        assertEquals(FAIL_TO_STOP, result.flow());
        assertEquals(DATE_TIME_FORMAT, result.errors().getFirst().messageKey().key());
        assertEquals(pattern, result.errors().getFirst().messageKey().args()[0]);
    }
}