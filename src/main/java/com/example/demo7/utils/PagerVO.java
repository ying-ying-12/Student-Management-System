package com.example.demo7.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 * 功能：封装分页相关数据和计算逻辑，支持前端页面的分页导航展示
 * @param <T> 泛型类型，指定分页数据的元素类型
 */
public class PagerVO<T> {
    private int current;       // 当前页码（从1开始）
    private int size;          // 每页显示的记录数
    private int total;         // 总记录数
    private List<T> list = new ArrayList<>();  // 当前页的数据列表

    private int totalPages;    // 总页数
    private boolean showLeft;  // 是否显示"上一页"按钮
    private boolean showRight; // 是否显示"下一页"按钮
    private int[] pageNums;    // 页码数组（用于展示连续页码）

    /**
     * 初始化分页参数
     * 计算总页数、页码范围和导航按钮显示状态
     */
    public void init() {
        // 计算总页数（总记录数/每页记录数，向上取整）
        totalPages = total / size;
        if (total % size > 0) {
            totalPages++;
        }

        // 判断是否显示"上一页"按钮（当前页为1时不显示）
        showLeft = current != 1;
        // 判断是否显示"下一页"按钮（当前页为最后一页时不显示）
        showRight = current != totalPages;

        // 计算页码显示范围（当前页前后各5页，共11个页码）
        int min = current - 5;
        int max = current + 5;

        // 防止最小页码小于1
        if (min < 1) {
            min = 1;
        }
        // 防止最大页码超过总页数
        if (max > totalPages) {
            max = totalPages;
        }

        // 生成页码数组
        int length = max - min + 1;
        pageNums = new int[length];
        for (int i = 0; i < length; i++) {
            pageNums[i] = min + i;
        }
    }

    // 以下为各属性的getter和setter方法，添加简要注释说明功能

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isShowLeft() {
        return showLeft;
    }

    public void setShowLeft(boolean showLeft) {
        this.showLeft = showLeft;
    }

    public boolean isShowRight() {
        return showRight;
    }

    public void setShowRight(boolean showRight) {
        this.showRight = showRight;
    }

    public int[] getPageNums() {
        return pageNums;
    }

    public void setPageNums(int[] pageNums) {
        this.pageNums = pageNums;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        // 防止空指针，若传入null则初始化为空列表
        this.list = list != null ? list : new ArrayList<>();
    }
}