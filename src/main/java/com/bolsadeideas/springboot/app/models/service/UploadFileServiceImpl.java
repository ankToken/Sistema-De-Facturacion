package com.bolsadeideas.springboot.app.models.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	@Override
	public Resource load(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String copy(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

}
