package com.drevish.social.controller;

import com.drevish.social.config.CustomAccessDeniedHandler;
import com.drevish.social.exception.MyFileNotFoundException;
import com.drevish.social.model.entity.File;
import com.drevish.social.service.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {FileController.class, CustomAccessDeniedHandler.class})
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class FileControllerTest {
    @MockBean
    private FileStorageService storageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnFile() throws Exception {
        String id = "id";
        File testFile = new File("image.png", "image/PNG", "example".getBytes());
        when(storageService.getFile(id)).thenReturn(testFile);
        mockMvc.perform(get("/file/{id}", id)
                .contentType(MediaType.IMAGE_PNG))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundResponse() throws Exception {
        String id = "id";
        when(storageService.getFile(id)).thenThrow(MyFileNotFoundException.class);
        mockMvc.perform(get("/file/{id}", id))
                .andExpect(status().isNotFound());
    }
}