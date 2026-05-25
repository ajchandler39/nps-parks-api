package com.example.demo.dto;

import java.util.List;

public class NpsResponse {

    private String total;
    private String limit;
    private String start;
    private List<ParkDto> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<ParkDto> getData() {
        return data;
    }

    public void setData(List<ParkDto> data) {
        this.data = data;
    }
}