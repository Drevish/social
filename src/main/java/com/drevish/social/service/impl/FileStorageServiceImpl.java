package com.drevish.social.service.impl;

import com.drevish.social.exception.FileStorageException;
import com.drevish.social.exception.MyFileNotFoundException;
import com.drevish.social.model.entity.File;
import com.drevish.social.model.repository.FileRepository;
import com.drevish.social.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public File saveFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("File name contains invalid path sequence " + fileName);
            }

            File myFile = new File(fileName, file.getContentType(), file.getBytes());
            return fileRepository.save(myFile);
        } catch (IOException e) {
            log.error("Error while storing file: " + fileName);
            throw new FileStorageException("Could not store file");
        }
    }

    @Override
    public File getFile(String id) {
        return fileRepository.findById(id).orElseThrow(
                () -> new MyFileNotFoundException("File not found with id " + id)
        );
    }

    @Override
    public void delete(File file) {
        fileRepository.delete(file);
    }
}
