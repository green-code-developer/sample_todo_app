package jp.green_code.todo.web.form;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
public class AppPageable {

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

    int toPageNumber() {
        return Math.max(0, currentPage - 1);
    }

    public PageRequest toPageRequest(Sort sort) {
        return PageRequest.of(toPageNumber(), getLimit(), sort);
    }
}
