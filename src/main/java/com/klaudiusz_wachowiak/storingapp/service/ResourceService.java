package com.klaudiusz_wachowiak.storingapp.service;

import com.klaudiusz_wachowiak.storingapp.dto.ResourceDownloadDto;
import com.klaudiusz_wachowiak.storingapp.dto.ResourceInDto;
import com.klaudiusz_wachowiak.storingapp.dto.ResourceOutDto;
import com.klaudiusz_wachowiak.storingapp.mapper.ResourceDownloadDtoMapper;
import com.klaudiusz_wachowiak.storingapp.mapper.ResourceMapper;
import com.klaudiusz_wachowiak.storingapp.mapper.ResourceOutDtoMapper;
import com.klaudiusz_wachowiak.storingapp.repository.ResourceRepository;
import com.klaudiusz_wachowiak.storingapp.util.DatabaseDownloader;
import com.klaudiusz_wachowiak.storingapp.util.ResourceUtil;
import com.klaudiusz_wachowiak.storingapp.util.exception.DataBaseDuplicateException;
import com.klaudiusz_wachowiak.storingapp.util.exception.NoConnectionException;
import com.klaudiusz_wachowiak.storingapp.util.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    private final ResourceOutDtoMapper resourceOutDtoMapper;
    private final ResourceDownloadDtoMapper resourceDownloadDtoMapper;
    private final ResourceMapper resourceMapper;

    private final ResourceUtil resourceUtil;
    private final DatabaseDownloader databaseDownloader;


    public ResourceService(ResourceRepository resourceRepository, ResourceOutDtoMapper resourceOutDtoMapper, ResourceDownloadDtoMapper resourceDownloadDtoMapper, ResourceMapper resourceMapper, ResourceUtil resourceUtil, DatabaseDownloader databaseDownloader) {
        this.resourceRepository = resourceRepository;
        this.resourceOutDtoMapper = resourceOutDtoMapper;
        this.resourceDownloadDtoMapper = resourceDownloadDtoMapper;
        this.resourceMapper = resourceMapper;
        this.resourceUtil = resourceUtil;
        this.databaseDownloader = databaseDownloader;
    }

    public Iterable<ResourceOutDto> getListOfResources() {
        return StreamSupport.stream(resourceRepository.findAll().spliterator(), true)
                            .map(resourceOutDtoMapper)
                            .collect(Collectors.toList());
    }

    public ResourceOutDto getResourceById(long id) {
        if (resourceRepository.findById(id).isPresent()) {
            return resourceOutDtoMapper.apply(resourceRepository.findById(id).get());
        }
        throw new NotFoundException();
    }

    @Transactional
    public Iterable<ResourceOutDto> getResourcesLike(String phrase) {
        List<ResourceOutDto> result = StreamSupport.stream(resourceRepository.findLike(phrase).spliterator(), true)
                                                   .map(resourceOutDtoMapper)
                                                   .collect(Collectors.toList());
        if (result.size() > 0) {
            return result;
        }
        throw new NotFoundException();
    }

    public ResourceOutDto addNewContent(ResourceInDto resourceInDto) throws IOException {

        if (resourceUtil.isConnection(resourceInDto.getUrl())) {
            if (!(
                    resourceRepository.existsByOriginUrlAddress(resourceInDto.getUrl()) ||
                            resourceRepository.existsByName(resourceInDto.getName())
            )
            ) {
                if (resourceUtil.getContentType(resourceInDto.getUrl()).contains("text/html")) {
                    log.info("saving: " + resourceInDto.getName());
                    return resourceOutDtoMapper.apply(resourceRepository.save(resourceMapper.apply(resourceInDto)));
                } else {
                    return resourceOutDtoMapper.apply(resourceRepository.save(databaseDownloader.downloadContent(resourceInDto)));
                }
            }
            throw new DataBaseDuplicateException();
        }
        throw new NoConnectionException();
    }

    @Transactional
    public ResourceDownloadDto getByName(String name) {
        if (resourceRepository.findByName(name).isPresent()) {
            return resourceDownloadDtoMapper.apply(resourceRepository.findByName(name).get());
        }
        throw new NotFoundException();
    }

    @Transactional
    public Iterable<ResourceOutDto> getAllByNames(String[] names) {
        if (Arrays.stream(names).noneMatch(s -> resourceRepository.findByName(s).isPresent())) {
            throw new NotFoundException();
        } else {
            return Arrays.stream(names)
                         .filter(s -> resourceRepository.findByName(s).isPresent())
                         .map(resourceRepository::findByName)
                         .map(Optional::get)
                         .map(resourceOutDtoMapper).collect(Collectors.toList());
        }
    }
}
