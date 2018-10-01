package com.klaudiusz_wachowiak.storingapp.mapper;

import com.klaudiusz_wachowiak.storingapp.dto.ResourceInDto;
import com.klaudiusz_wachowiak.storingapp.model.ResourceModel;
import com.klaudiusz_wachowiak.storingapp.util.ResourceUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

@Component
public class ResourceMapper implements Function<ResourceInDto, ResourceModel> {

    private final ResourceUtil resourceUtil;

    public ResourceMapper(ResourceUtil resourceUtil) {
        this.resourceUtil = resourceUtil;
    }

    @Override
    public ResourceModel apply(ResourceInDto resourceInDto) {
        ResourceModel resourceModel = new ResourceModel();

        resourceModel.setName(resourceInDto.getName());
        if (resourceInDto.getDescription().equals("") || resourceInDto.getDescription() == null) {
            resourceModel.setDescription(null);
        } else {
            resourceModel.setDescription(resourceInDto.getDescription());
        }
        if (resourceInDto.getFileName().equals("") || resourceInDto.getFileName() == null) {
            resourceModel.setFileName(resourceInDto.getName());
        } else {
            resourceModel.setFileName(resourceInDto.getFileName());
        }
        resourceModel.setOriginUrlAddress(resourceInDto.getUrl());
        try {
            resourceModel.setContent(resourceUtil.getContent(resourceInDto.getUrl()));
            resourceModel.setContentType(resourceUtil.getContentType(resourceInDto.getUrl()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resourceModel;
    }
}
