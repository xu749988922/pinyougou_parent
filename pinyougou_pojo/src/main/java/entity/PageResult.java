package entity;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {
    private Long pages;
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(Long pages, List<T> rows) {
        this.pages = pages;
        this.rows = rows;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
