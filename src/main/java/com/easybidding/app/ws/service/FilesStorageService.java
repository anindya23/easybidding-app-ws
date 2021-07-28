package com.easybidding.app.ws.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

//	public String storeFile(MultipartFile file);

	public Resource loadFileAsResource(String filename);
	
}