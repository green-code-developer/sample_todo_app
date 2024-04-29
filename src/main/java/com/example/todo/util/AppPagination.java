package com.example.todo.util;

// pagination.html にて使用
public class AppPagination {

    // 現在ページ数
    public int current;

    // 総データ数（ページ数ではない）
    public long count;

    // 1ページ辺り件数
    public int pageSize;

    // 前後表示数、デフォルト2
    public int around;

    public AppPagination(int current, long count, int pageSize) {
        this(current, count, pageSize, 2);
    }

    public AppPagination(int current, long count, int pageSize, int around) {
        this.count = count;
        this.pageSize = Math.max(1, pageSize);
        this.current = Math.min(current, getTotal());
        this.around = around;
    }

    public static AppPagination init(int current, long count, int pageSize, Integer around) {
        return new AppPagination(current, count, pageSize, around);
    }

    // 総ページ数
    public int getTotal() {
        return count == 0 ? 0 : (int) Math.ceil((double) count / pageSize);
    }

    // 前後表示の低い方、マイナスにならない
    public int getLower() {
        return Math.max(1, current - around);
    }

    // 前後表示の高い方、総ページ数を声ない
    public int getUpper() {
        return Math.min(getTotal(), current + around);
    }
}
