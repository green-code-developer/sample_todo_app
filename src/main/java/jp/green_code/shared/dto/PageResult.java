package jp.green_code.shared.dto;

import java.util.List;

/**
 * pagination.html にて使用
 */
public record PageResult<T>(
        // ページ指定
        PageRequest pageRequest,
        // データ
        List<T> list,
        // 総データ件数
        long totalCount,
        // 前後表示数、デフォルト2
        int pageRange) {
    public PageResult(PageRequest pageRequest, List<T> list, long totalCount) {
        this(pageRequest, list, totalCount, 2);
    }

    // 現在ページ数
    public int getCurrentPage() {
        return pageRequest.getCurrentPage();
    }

    // 1ページ辺り件数
    public int getPageSize() {
        return pageRequest.getPageSize();
    }

    // 総ページ数
    public int getTotalPage() {
        return totalCount == 0 ? 0 : (int) Math.ceil((double) totalCount / getPageSize());
    }

    // 前後表示の低い方、マイナスにならない
    public int getLower() {
        return Math.max(1, getCurrentPage() - pageRange);
    }

    // 前後表示の高い方、総ページ数を越えない
    public int getUpper() {
        return Math.min(getTotalPage(), getCurrentPage() + pageRange);
    }
}
