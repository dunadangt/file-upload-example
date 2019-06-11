package com.bytesw.inventarios.fileuploadexample.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bytesw.inventarios.fileuploadexample.service.UploadFileService;

@RestController
@RequestMapping(path = "/uploads")
public class UploadMultipartFileController {
	
	@Autowired
	private UploadFileService uploadFileService;

	@PostMapping(path = "/local")
    public String uploadingPostLocal(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles) throws IOException {
        for(MultipartFile uploadedFile : uploadingFiles) {
            uploadFileService.uploadLocal(uploadedFile.getBytes());
        }

        return "OK";
    }
	
	@PostMapping(path = "/remote")
    public String uploadingPostRemote(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles) throws IOException {
        for(MultipartFile uploadedFile : uploadingFiles) {
           uploadFileService.uploadRemote(uploadedFile.getBytes());
        }

        return "OK";
    }
	
}
