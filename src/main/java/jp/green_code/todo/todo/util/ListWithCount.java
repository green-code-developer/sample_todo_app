package jp.green_code.todo.todo.util;

import java.util.List;

public class ListWithCount<T> {
    private List<T> list;

    private long count;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
