package com.example.demo.service;

import com.example.demo.model.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.FileUploadRepo;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UploadFileService {

    @Autowired
    private FileUploadRepo fileUploadRepo;

	public List<FileModel> uploadFiles( List<MultipartFile> files) throws IOException {
        for(MultipartFile file : files){
            FileModel fileMetaData = new FileModel();
            fileMetaData.setFileId(UUID.randomUUID().toString());
            fileMetaData.setFileName(file.getOriginalFilename());
            fileMetaData.setFileContentType(file.getContentType());
            fileMetaData.setFileSize(file.getSize());
            fileMetaData.setFileData(file.getBytes());

            fileUploadRepo.save(fileMetaData);
        }
        return fileUploadRepo.findAll();
    }

    public String deleteFile(String id){
        fileUploadRepo.deleteById(id);
        return "File Deleted Successfully.";
    }

    public FileModel downloadFile(String id){
        return fileUploadRepo.findById(id).orElse(null);
    }
}
