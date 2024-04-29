package com.martishyn.usersapi.dto.user;

import java.util.List;

public class ListDataResponseWrapper {
    private List<ResponseUserDto> data;

    public ListDataResponseWrapper(List<ResponseUserDto> responseList) {
        this.data = responseList;
    }

    public List<ResponseUserDto> getData() {
        return data;
    }

    public void setData(List<ResponseUserDto> data) {
        this.data = data;
    }
}
