package jp.green_code.shared.dto;

import java.util.List;

import static jp.green_code.shared.dto.ValidationStepResult.ValidationFlow.FAIL_TO_STOP;
import static jp.green_code.shared.dto.ValidationStepResult.ValidationFlow.OK;
import static jp.green_code.shared.dto.ValidationStepResult.ValidationFlow.OK_BUT_STOP;

public record ValidationStepResult(ValidationFlow flow, List<ValidationError> errors) {

    public enum ValidationFlow {
        // 正常（後続を実行）
        OK(0, true, false),
        // 正常だが後続を実行しない
        OK_BUT_STOP(1, true, true),
        // 異常だが後続を実行する
        FAIL_BUT_CONTINUE(2, false, false),
        // 異常終了（後続を実行しない）
        FAIL_TO_STOP(3, false, true)
        ;
        private final int severity;
        private final boolean ok;
        private final boolean stop;

        ValidationFlow(int severity, boolean ok, boolean stop) {
            this.severity = severity;
            this.ok = ok;
            this.stop = stop;
        }

        public int getSeverity() {
            return severity;
        }

        public boolean isStop() {
            return stop;
        }

        public boolean isOk() {
            return ok;
        }
    }

    public static final ValidationStepResult OK_RESULT = new ValidationStepResult(OK, new ValidationError(new MessageKey("")));
    public static final ValidationStepResult OK_BUT_STOP_RESULT = new ValidationStepResult(OK_BUT_STOP, new ValidationError(new MessageKey("")));

    private ValidationStepResult(ValidationFlow flow, ValidationError error) {
        this(flow, List.of(error));
    }

    public static ValidationStepResult failButContinue(ValidationError error) {
        return new ValidationStepResult(ValidationFlow.FAIL_BUT_CONTINUE, error);
    }

    public static ValidationStepResult failToStop(ValidationError error) {
        return new ValidationStepResult(FAIL_TO_STOP, error);
    }

    public boolean ok() {
        return flow.isOk();
    }
}
