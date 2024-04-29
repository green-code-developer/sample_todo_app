package com.example.todo.web.form;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Todo 検索条件フォーム
 */
public class TodoSearchForm {

    private String word;
    private String status;
    private String deadlineFrom;
    private String deadlineTo;
    private int currentPage = 1;
    private int pageSize = 10;

    private String sort;

    // 画面上の検索条件を開いておくかどうか
    public boolean isNoCondition() {
        return isBlank(word) && isBlank(status)
            && isBlank(deadlineFrom) && isBlank(deadlineTo)
            && isBlank(sort);
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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
