package jp.green_code.shared.dto;

/**
 * ページングリクエスト
 *   Form が継承して使えるようにrecord ではなくclass で作成
 */
public class PageRequest {

    public final int MAX_PAGE_SIZE = 1000;
    public final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 現在ページ（1始まり）
     */
    private int currentPage = 1;

    /**
     * 1ページあたり件数
     */
    private int pageSize = 10;

    public int getOffset() {
        return pageSize * (currentPage - 1);
    }

    public int getLimit() {
        // 大量に取るとメモリを食い潰すので必ず上限を設定する
        return Math.min(pageSize, MAX_PAGE_SIZE);
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
        // 入口で上限を適用する
        // getOffset() や総ページ数の計算も同じ値を使うため、ここで丸めないとページング計算が食い違う
        this.pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
    }
}