package jp.green_code.todo.dto.common;

import lombok.Data;

import java.util.List;

/**
 * pagination.html にて使用
 */
@Data
public class AppPageableList<T> {

    // データ
    private final List<T> list;

    // 総データ件数
    private final long count;

    // ページ指定
    private AppPageableDto pageableDto;

    // 前後表示数、デフォルト2
    private int around = 2;

    public AppPageableList(AppPageableDto pageableDto, List<T> list, long count) {
        this.pageableDto = pageableDto;
        this.list = list;
        this.count = count;
    }

    public static <T> AppPageableList<T> empty() {
        return new AppPageableList<>(new AppPageableDto(), List.of(), 0);
    }

    // 現在ページ数
    public int getCurrentPage() {
        return pageableDto.getCurrentPage();
    }

    // 1ページ辺り件数
    public int getPageSize() {
        return pageableDto.getPageSize();
    }

    // 総ページ数
    public int getTotalPage() {
        return count == 0 ? 0 : (int) Math.ceil((double) count / getPageSize());
    }

    // 前後表示の低い方、マイナスにならない
    public int getLower() {
        return Math.max(1, getCurrentPage() - around);
    }

    // 前後表示の高い方、総ページ数を越えない
    public int getUpper() {
        return Math.min(getTotalPage(), getCurrentPage() + around);
    }
}
