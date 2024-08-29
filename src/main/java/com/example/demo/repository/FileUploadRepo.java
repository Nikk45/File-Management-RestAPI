package com.example.demo.repository;

import com.example.demo.model.FileModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepo extends MongoRepository<FileModel, String> {
}
