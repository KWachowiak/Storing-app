package com.klaudiusz_wachowiak.storingapp.util;

import com.klaudiusz_wachowiak.storingapp.dto.ResourceInDto;
import com.klaudiusz_wachowiak.storingapp.mapper.ResourceMapper;
import com.klaudiusz_wachowiak.storingapp.model.ResourceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class DatabaseDownloader {

    private final ResourceMapper resourceMapper;

    public DatabaseDownloader(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    public synchronized ResourceModel downloadContent(ResourceInDto resource) {
        log.info("Current downloading file: " + resource.getName());
        return resourceMapper.apply(resource);
    }
}
