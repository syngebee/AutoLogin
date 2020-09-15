package com.itheima.pojo;

import java.util.List;

public class PageBean<T> {
    private String totalCount;
    private String totalPage;
    private List<T> showList;
    private String current;
    private String size;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getShowList() {
        return showList;
    }

    public void setShowList(List<T> showList) {
        this.showList = showList;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
