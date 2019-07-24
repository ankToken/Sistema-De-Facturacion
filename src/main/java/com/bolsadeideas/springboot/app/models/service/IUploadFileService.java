package com.bolsadeideas.springboot.app.models.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	Resource load(String fileName);
	
	String copy(MultipartFile file);
	
	boolean delete(String fileName);
}
