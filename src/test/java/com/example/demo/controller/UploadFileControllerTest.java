package com.example.demo.controller;

import com.example.demo.model.FileModel;
import com.example.demo.service.UploadFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UploadFileControllerTest {

    @Mock
    UploadFileService uploadFileService;

    @InjectMocks
    UploadFileController uploadFileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testHello() {
//        when(uploadFileController.Hello()).thenReturn("WOrking correctly........");
//        assertEquals("WOrking correctly............", uploadFileController.Hello());
//    }

    @Test
    void uploadFilesControllerTest() throws IOException {

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);

        when(file1.getOriginalFilename()).thenReturn("file1.txt");
        when(file1.getContentType()).thenReturn("text/plain");
        when(file1.getSize()).thenReturn(123L);
        when(file1.getBytes()).thenReturn("file content 1".getBytes());

        when(file2.getOriginalFilename()).thenReturn("file2.txt");
        when(file2.getContentType()).thenReturn("text/plain");
        when(file2.getSize()).thenReturn(456L);
        when(file2.getBytes()).thenReturn("file content 2".getBytes());

        List<MultipartFile> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);

        FileModel fileModel1 = new FileModel();
        fileModel1.setFileId(UUID.randomUUID().toString());
        fileModel1.setFileName("file1.txt");
        fileModel1.setFileContentType("text/plain");
        fileModel1.setFileSize(123L);
        fileModel1.setFileData("file content 1".getBytes());

        FileModel fileModel2 = new FileModel();
        fileModel2.setFileId(UUID.randomUUID().toString());
        fileModel2.setFileName("file2.txt");
        fileModel2.setFileContentType("text/plain");
        fileModel2.setFileSize(456L);
        fileModel2.setFileData("file content 2".getBytes());

        when(uploadFileService.uploadFiles(any())).thenReturn(List.of(fileModel1,fileModel2));

        ResponseEntity<List<FileModel>> response = uploadFileController.uploadFiles(files);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<FileModel> responseBody = response.getBody();
        assertNotNull(responseBody);                    // check if responseBody is not null
        assertEquals(2, responseBody.size());        // checking the size of the responseBOdy is equals to 2


        FileModel responseFile1 = responseBody.get(0);
        FileModel responseFile2 = responseBody.get(1);

        assertEquals("file1.txt", responseFile1.getFileName());
        assertEquals("file2.txt", responseFile2.getFileName());

    }

    @Test
    void deleteFileControllerTest(){
        String fileId = "12345";

        when(uploadFileService.deleteFile(anyString())).thenReturn("File Deleted Successfully.");

        String result = uploadFileController.deleteFile(fileId);

        assertEquals("File Deleted Successfully.", result);

    }

    @Test
    void downloadFileControllerTest() throws IOException {

        FileModel fileModel = new FileModel();
        fileModel.setFileId(UUID.randomUUID().toString());
        fileModel.setFileName("file1.txt");
        fileModel.setFileContentType("text/plain");
        fileModel.setFileSize(123L);
        fileModel.setFileData("file content 1".getBytes());

        when(uploadFileService.downloadFile(any())).thenReturn((fileModel));

        FileModel result = uploadFileController.getFileData("1234");

        assertEquals("file1.txt", result.getFileName());
    }

}
