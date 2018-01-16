package com.langbai.tdhd.bean;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/10/20.
 */

public class SearchBean {
    private int total;
    private ArrayList<LoginResponseBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<LoginResponseBean> getRows() {
        return rows;
    }

    public void setRows(ArrayList<LoginResponseBean> rows) {
        this.rows = rows;
    }
}
