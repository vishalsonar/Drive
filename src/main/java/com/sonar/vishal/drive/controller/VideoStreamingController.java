package com.sonar.vishal.drive.controller;

import com.sonar.vishal.drive.util.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/videos")
public class VideoStreamingController {

    @Value("${drive.stream.folder}")
    private String streamFolder;

    @GetMapping("/stream/{fileName}")
    public FileSystemResource videoSource(@PathVariable(value = "fileName") String fileName) {
        return new FileSystemResource(new File(streamFolder + Constant.FOLDER_SLASH + fileName));
    }
}
