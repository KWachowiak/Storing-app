package com.klaudiusz_wachowiak.storingapp.mapper;

import com.klaudiusz_wachowiak.storingapp.dto.ResourceOutDto;
import com.klaudiusz_wachowiak.storingapp.model.ResourceModel;
import com.klaudiusz_wachowiak.storingapp.util.MimeToExtensionConverter;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ResourceOutDtoMapper implements Function<ResourceModel, ResourceOutDto> {

    private final MimeToExtensionConverter converter;

    public ResourceOutDtoMapper(MimeToExtensionConverter converter) {
        this.converter = converter;
    }

    @Override
    public ResourceOutDto apply(ResourceModel resourceModel) {
        ResourceOutDto resourceOutDto = new ResourceOutDto();

        resourceOutDto.setId(resourceModel.getId());
        resourceOutDto.setName(resourceModel.getName());
        resourceOutDto.setDescription(resourceModel.getDescription());
        resourceOutDto.setUrl(resourceModel.getOriginUrlAddress());
        resourceOutDto.setFileName(resourceModel.getFileName());
        try {
            resourceOutDto.setExtension(converter.convertToExtension(resourceModel.getContentType()));
        } catch (MimeTypeException e) {
            resourceOutDto.setExtension("Converting ERROR");
        }

        return resourceOutDto;
    }
}
