package jp.green_code.todo.todo.web.form;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Todo 検索条件フォーム
 */
public class TodoSearchForm extends AppPageable {

    private String word;
    private String status;
    private String deadlineFrom;
    private String deadlineTo;
    private String sort;

    // 画面上の検索条件を開いておくかどうか
    public boolean isNoCondition() {
        return isBlank(word) && isBlank(status)
            && isBlank(deadlineFrom) && isBlank(deadlineTo)
            && isBlank(sort) && getPageSize() == 10;
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
