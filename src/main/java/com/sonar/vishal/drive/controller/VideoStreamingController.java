package com.sonar.vishal.drive.controller;

import com.sonar.vishal.drive.context.Context;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/videos")
public class VideoStreamingController {

    @Autowired
    private transient Logger logger;

    @Autowired
    private CacheController cacheController;

    @GetMapping("/stream/{fileName}")
    public ResponseEntity<ByteArrayResource> videoSource(@PathVariable(value = "fileName") String fileName) {
        ResponseEntity<ByteArrayResource> responseEntity = ResponseEntity.noContent().build();
        try {
            ByteArrayResource resource = Context.getBean(ByteArrayResource.class, cacheController.get(fileName));
            responseEntity = ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString())
                    .body(resource);
        } catch (Exception exception) {
            logger.error("Error while streaming video", exception);
        }
        return responseEntity;
    }
}
