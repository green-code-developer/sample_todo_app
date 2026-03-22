package jp.green_code.todo.domain.dto;

import jp.green_code.shared.dto.PageRequest;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Todo 検索条件フォーム
 */
public class TodoSearchDto extends PageRequest {

    private String word;

    private String status;

    private String deadlineFrom;

    private String deadlineTo;

    private String sort;

    // 画面上の検索条件を開いておくかどうか
    public boolean isNoCondition() {
        return isBlank(word) && isBlank(status) && isBlank(deadlineFrom) && isBlank(deadlineTo) && isBlank(sort) && getPageSize() == DEFAULT_PAGE_SIZE;
    }

    public String toLogString() {
        return String.format("TodoSearchForm(word=%s,status=%s,deadlineFrom=%s,deadlineTo=%s,sort=%s,page=%s,size=%s)", StringUtils.left(word, 100), status, deadlineFrom, deadlineTo, sort, getCurrentPage(), getPageSize());
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeadlineFrom() {
        return deadlineFrom;
    }

    public void setDeadlineFrom(String deadlineFrom) {
        this.deadlineFrom = deadlineFrom;
    }

    public String getDeadlineTo() {
        return deadlineTo;
    }

    public void setDeadlineTo(String deadlineTo) {
        this.deadlineTo = deadlineTo;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
