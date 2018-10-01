package com.klaudiusz_wachowiak.storingapp.dto;

import lombok.Data;

@Data
public class ResourceDownloadDto {
    private String fileName;
    private String contentType;
    private String extension;
    private byte[] content;
}
