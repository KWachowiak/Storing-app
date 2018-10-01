package com.klaudiusz_wachowiak.storingapp.mapper;

import com.klaudiusz_wachowiak.storingapp.dto.ResourceDownloadDto;
import com.klaudiusz_wachowiak.storingapp.model.ResourceModel;
import com.klaudiusz_wachowiak.storingapp.util.MimeToExtensionConverter;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ResourceDownloadDtoMapper implements Function<ResourceModel, ResourceDownloadDto> {

    private final MimeToExtensionConverter converter;

    public ResourceDownloadDtoMapper(MimeToExtensionConverter converter) {
        this.converter = converter;
    }

    @Override
    public ResourceDownloadDto apply(ResourceModel resourceModel) {
        ResourceDownloadDto resourceDownloadDto = new ResourceDownloadDto();

        resourceDownloadDto.setFileName(resourceModel.getFileName());
        resourceDownloadDto.setContentType(resourceModel.getContentType());
        resourceDownloadDto.setContent(resourceModel.getContent());
        try {
            resourceDownloadDto.setExtension(converter.convertToExtension(resourceDownloadDto.getContentType()));
        } catch (MimeTypeException e) {
            resourceDownloadDto.setExtension("");
        }

        return resourceDownloadDto;
    }
}
