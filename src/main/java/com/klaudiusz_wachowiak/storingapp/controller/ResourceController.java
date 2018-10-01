package com.klaudiusz_wachowiak.storingapp.controller;

import com.klaudiusz_wachowiak.storingapp.dto.ResourceDownloadDto;
import com.klaudiusz_wachowiak.storingapp.dto.ResourceInDto;
import com.klaudiusz_wachowiak.storingapp.dto.ResourceOutDto;
import com.klaudiusz_wachowiak.storingapp.service.ResourceService;
import com.klaudiusz_wachowiak.storingapp.util.exception.DataBaseDuplicateException;
import com.klaudiusz_wachowiak.storingapp.util.exception.NoConnectionException;
import com.klaudiusz_wachowiak.storingapp.util.exception.NotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ResponseEntity<Iterable<ResourceOutDto>> getAllResources() {
        return ResponseEntity.ok(resourceService.getListOfResources());
    }

    @GetMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<?> getResourceById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(resourceService.getResourceById(id));
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(params = "like")
    public ResponseEntity<?> getResourceByNameOrFileNameOrUrl(@RequestParam("like") String phrase) {
        try {
            return ResponseEntity.ok(resourceService.getResourcesLike(phrase));
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/download/{name}")
    public ResponseEntity<?> downloadContents(@PathVariable String name) {

        try {
            ResourceDownloadDto resourceDownloadDto = resourceService.getByName(name);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", (resourceDownloadDto.getFileName() + resourceDownloadDto.getExtension())));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(resourceDownloadDto.getContent()));

            return ResponseEntity.ok()
                                 .headers(headers)
                                 .contentLength(resourceDownloadDto.getContent().length)
                                 .contentType(MediaType.parseMediaType(resourceDownloadDto.getContentType()))
                                 .body(resource);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/download/many/{names}")
    public ResponseEntity<?> getListOfResources(@PathVariable String[] names) {
        try {
            return ResponseEntity.ok(resourceService.getAllByNames(names));
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addNewResourceToDB(@RequestBody ResourceInDto resourceInDto) {
        try {
            return new ResponseEntity<>(resourceService.addNewContent(resourceInDto), HttpStatus.OK);
        } catch (ConstraintViolationException | NullPointerException e) {
            return new ResponseEntity<>("ERROR - Wrong JSON Template", HttpStatus.BAD_REQUEST);
        } catch (NoConnectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_GATEWAY);
        } catch (DataBaseDuplicateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IOException e) {
            return new ResponseEntity<>(new NoConnectionException().getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
