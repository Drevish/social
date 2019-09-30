package com.drevish.social.service.impl;

import com.drevish.social.exception.FileStorageException;
import com.drevish.social.exception.MyFileNotFoundException;
import com.drevish.social.model.entity.File;
import com.drevish.social.model.repository.FileRepository;
import com.drevish.social.service.FileStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class FileStorageServiceImplTest {

    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private File testFile;

    @Before
    public void before() {
        testFile = new File("name", "image", "Some data".getBytes());
        testFile.setId("unique id");
    }

    @Test
    public void shouldSaveFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("");
        fileStorageService.saveFile(file);
        verify(fileRepository, times(1)).save(any(File.class));
    }

    @Test(expected = FileStorageException.class)
    public void shouldThrowFileStorageException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("");
        when(file.getBytes()).thenThrow(IOException.class);
        fileStorageService.saveFile(file);
    }

    @Test
    public void shouldGetFile() {
        when(fileRepository.findById(testFile.getId())).thenReturn(Optional.of(testFile));
        fileStorageService.getFile(testFile.getId());
        verify(fileRepository, times(1)).findById(testFile.getId());
    }

    @Test(expected = MyFileNotFoundException.class)
    public void shouldThrowExceptionIfFileNotFound() {
        when(fileRepository.findById(testFile.getId())).thenReturn(Optional.empty());
        fileStorageService.getFile(testFile.getId());
    }

    @Test
    public void shouldDeleteFile() {
        fileStorageService.delete(testFile);
        verify(fileRepository, times(1)).delete(testFile);
    }

    @TestConfiguration
    static class FileStorageServiceImplTestContextConfiguration {
        @Bean
        public FileStorageService fileStorageService() {
            return new FileStorageServiceImpl();
        }
    }
}