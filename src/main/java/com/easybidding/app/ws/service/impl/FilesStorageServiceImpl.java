package com.easybidding.app.ws.service.impl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.easybidding.app.ws.exception.FileNotFoundException;
import com.easybidding.app.ws.properties.FileStorageProperties;
import com.easybidding.app.ws.service.FilesStorageService;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	@Autowired
	FileStorageProperties fileStorageProperties;

	@Override
	public Resource loadFileAsResource(String fileName) {
		try {
			Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
			Path filePath = fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new FileNotFoundException("File not found " + fileName, ex);
		}
	}

}