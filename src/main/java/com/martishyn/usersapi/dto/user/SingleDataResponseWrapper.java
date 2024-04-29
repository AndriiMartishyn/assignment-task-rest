package com.martishyn.usersapi.dto.user;

public class SingleDataResponseWrapper{
    private ResponseUserDto data;

    public SingleDataResponseWrapper(ResponseUserDto data) {
        this.data = data;
    }

    public ResponseUserDto getData() {
        return data;
    }

    public void setData(ResponseUserDto data) {
        this.data = data;
    }
}
