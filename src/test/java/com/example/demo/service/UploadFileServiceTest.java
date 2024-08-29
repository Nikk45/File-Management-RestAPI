package com.example.demo.service;

import com.example.demo.model.FileModel;
import com.example.demo.repository.FileUploadRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UploadFileServiceTest {

    @Mock
    private FileUploadRepo fileUploadRepo;

    @InjectMocks
    private UploadFileService uploadFileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void uploadFilesTestSuccess() throws IOException{

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

        // Create FileModel instances with expected data
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

        // Mock repository behavior
        when(fileUploadRepo.save(any(FileModel.class)))
                .thenReturn(fileModel1)
                .thenReturn(fileModel2);
        when(fileUploadRepo.findAll()).thenReturn(List.of(fileModel1, fileModel2));

        // Call the method to be tested
        List<FileModel> result = uploadFileService.uploadFiles(files);

        // Verify interactions and assertions
        verify(fileUploadRepo, times(2)).save(any(FileModel.class));
        assertEquals(2, result.size());
        assertEquals("file1.txt", result.get(0).getFileName());
        assertEquals("file2.txt", result.get(1).getFileName());

    }

    @Test
    void deleteFileTest(){
        String fileId = "12345";

        String result = uploadFileService.deleteFile(fileId);

        verify(fileUploadRepo,times(1)).deleteById(fileId);

        assertEquals("File Deleted Successfully.", result);

    }

    @Test
    void downloadFileTest() throws IOException {

        FileModel fileModel = new FileModel();
        fileModel.setFileId(UUID.randomUUID().toString());
        fileModel.setFileName("file1.txt");
        fileModel.setFileContentType("text/plain");
        fileModel.setFileSize(123L);
        fileModel.setFileData("file content 1".getBytes());

        when(fileUploadRepo.findById(any())).thenReturn(Optional.of(fileModel));

        FileModel result = uploadFileService.downloadFile("1234");

        assertEquals("file1.txt", result.getFileName());
    }
}
