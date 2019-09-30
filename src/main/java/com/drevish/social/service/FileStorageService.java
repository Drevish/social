package com.drevish.social.service;

import com.drevish.social.model.entity.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    File saveFile(MultipartFile file);

    File getFile(String id);

    void delete(File file);
}
