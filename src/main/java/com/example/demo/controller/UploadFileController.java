package com.example.demo.controller;

import com.example.demo.model.FileModel;
import com.example.demo.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/file")
public class UploadFileController {

	@Autowired
	private UploadFileService uploadFileService;

//	@GetMapping()
//	public String Hello() {
//		return "WOrking correctly............";
//	}

	@PostMapping("/upload")
	public ResponseEntity<List<FileModel>> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<FileModel> res = uploadFileService.uploadFiles(files);

		return ResponseEntity.ok(res);
	}

	@DeleteMapping("/delete-file/{id}")
	public String deleteFile(@PathVariable String id){
		return uploadFileService.deleteFile(id);
	}

	@GetMapping("/get-file/{id}")
	public FileModel getFileData(@PathVariable String id){
		return uploadFileService.downloadFile(id);
	}
}
