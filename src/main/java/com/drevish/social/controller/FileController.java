package com.drevish.social.controller;

import com.drevish.social.exception.MyFileNotFoundException;
import com.drevish.social.model.entity.File;
import com.drevish.social.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private FileStorageService storageService;

    @GetMapping("/{id}")
    public ResponseEntity getFile(@PathVariable String id) {
        File file;
        try {
            file = storageService.getFile(id);
        } catch (MyFileNotFoundException e) {
            log.error(e.toString());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getData()));
    }
}
