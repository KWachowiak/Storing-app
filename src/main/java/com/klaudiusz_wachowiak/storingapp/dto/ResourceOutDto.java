package com.klaudiusz_wachowiak.storingapp.dto;

import lombok.Data;

@Data
public class ResourceOutDto {

    private long id;
    private String name;
    private String description;
    private String url;
    private String fileName;
    private String extension;
}
