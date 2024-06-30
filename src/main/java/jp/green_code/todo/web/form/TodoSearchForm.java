package jp.green_code.todo.web.form;

import static jp.green_code.todo.util.DateUtil.parseYMD_hyphen_loose;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import java.util.Arrays;
import java.util.Optional;
import jp.green_code.todo.enums.TodoSearchSortEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * Todo 検索条件フォーム
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TodoSearchForm extends AppPageable {

    @Size(max = 100)
    private String word;

    @Size(max = 10)
    private String status;

    @Size(max = 10)
    private String deadlineFrom;

    @Size(max = 10)
    private String deadlineTo;

    private String sort;

    @AssertTrue(message = "{validation.date-format}")
    public boolean isAssertionOfDeadlineFrom() {
        return isEmpty(deadlineFrom) || parseYMD_hyphen_loose(deadlineFrom).isPresent();
    }

    @AssertTrue(message = "{validation.date-format}")
    public boolean isAssertionOfDeadlineTo() {
        return isEmpty(deadlineTo) || parseYMD_hyphen_loose(deadlineTo).isPresent();
    }

    @AssertTrue(message = "{validation.invalid-choice}")
    public boolean isAssertionOfSort() {
        return isEmpty(sort) || toSortEnum().isPresent();
    }

    public Optional<TodoSearchSortEnum> toSortEnum() {
        return Arrays.stream(TodoSearchSortEnum.values())
            .filter(e -> StringUtils.equals(e + "", sort)).findFirst();
    }

    // 画面上の検索条件を開いておくかどうか
    public boolean isNoCondition() {
        return isBlank(word) && isBlank(status)
            && isBlank(deadlineFrom) && isBlank(deadlineTo)
            && isBlank(sort) && getPageSize() == 10;
    }
}
