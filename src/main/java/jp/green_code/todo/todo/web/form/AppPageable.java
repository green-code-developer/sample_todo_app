package jp.green_code.todo.todo.web.form;

public class AppPageable {

    public static final int MAX = 1000;

    // 現在ページ 1始まり
    private int currentPage = 1;
    private int pageSize = 10;

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

    public int toOffset() {
        return pageSize * (currentPage - 1);
    }

    public int  toLimit() {
        // 大量に取るとメモリを食い潰すので必ず上限を設定する
        return Math.min(pageSize, MAX);
    }
}
