package jp.green_code.todo.dto.common;

import lombok.Data;

@Data
public class AppPageableDto {

    public static final int MAX = 1000;

    // 現在ページ 1始まり
    private int currentPage = 1;
    private int pageSize = 10;

    public int getOffset() {
        return pageSize * (currentPage - 1);
    }

    public int getLimit() {
        // 大量に取るとメモリを食い潰すので必ず上限を設定する
        return Math.min(pageSize, MAX);
    }
}
