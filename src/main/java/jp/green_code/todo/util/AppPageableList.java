package jp.green_code.todo.util;

import java.util.List;

// pagination.html にて使用
public class AppPageableList<T> {

    // データ
    public final List<T> list;

    // 総データ件数
    public final long count;

    // 現在ページ数
    public final int currentPage;

    // 1ページ辺り件数
    public final int pageSize;

    // 前後表示数、デフォルト2
    public int around = 2;

    public AppPageableList(List<T> list, long count, int currentPage, int pageSize) {
        this.list = list;
        this.count = count;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // 総ページ数
    public int getTotalPage() {
        return count == 0 ? 0 : (int) Math.ceil((double) count / pageSize);
    }

    // 前後表示の低い方、マイナスにならない
    public int getLower() {
        return Math.max(1, currentPage - around);
    }

    // 前後表示の高い方、総ページ数を越えない
    public int getUpper() {
        return Math.min(getTotalPage(), currentPage + around);
    }
}
