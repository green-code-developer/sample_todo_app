package jp.green_code.todo.domain.dto;

import org.jspecify.annotations.Nullable;

import jp.green_code.shared.dto.PageRequest;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Todo 検索条件フォーム
 */
public class TodoSearchDto extends PageRequest {

    @Nullable
    private String word;

    @Nullable
    private String status;

    @Nullable
    private String deadlineFrom;

    @Nullable
    private String deadlineTo;

    @Nullable
    private String sort;

    // 画面上の検索条件を開いておくかどうか
    public boolean isNoCondition() {
        return isBlank(word) && isBlank(status) && isBlank(deadlineFrom) && isBlank(deadlineTo) && isBlank(sort) && getPageSize() == DEFAULT_PAGE_SIZE;
    }

    public String toLogString() {
        return String.format("TodoSearchForm(word=%s,status=%s,deadlineFrom=%s,deadlineTo=%s,sort=%s,page=%s,size=%s)", StringUtils.left(word, 100), status, deadlineFrom, deadlineTo, sort, getCurrentPage(), getPageSize());
    }

    public @Nullable String getWord() {
        return word;
    }

    public void setWord(@Nullable String word) {
        this.word = word;
    }

    public @Nullable String getStatus() {
        return status;
    }

    public void setStatus(@Nullable String status) {
        this.status = status;
    }

    public @Nullable String getDeadlineFrom() {
        return deadlineFrom;
    }

    public void setDeadlineFrom(@Nullable String deadlineFrom) {
        this.deadlineFrom = deadlineFrom;
    }

    public @Nullable String getDeadlineTo() {
        return deadlineTo;
    }

    public void setDeadlineTo(@Nullable String deadlineTo) {
        this.deadlineTo = deadlineTo;
    }

    public @Nullable String getSort() {
        return sort;
    }

    public void setSort(@Nullable String sort) {
        this.sort = sort;
    }
}
