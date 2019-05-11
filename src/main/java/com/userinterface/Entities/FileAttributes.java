package com.userinterface.Entities;

import lombok.Data;

@Data
public class FileAttributes {
    private String name;
    private long size;
    private String url;
    private String durl;

    public FileAttributes(String name, long size, String url, String durl) {
        this.name = name;
        this.size = size;
        this.url = url;
        this.durl = durl;
    }
}
