package com.webprogramming.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {
 
	@Value("${app.upload.dir}")
	private String uploadDir;
 
	public String store(MultipartFile file, String subFolder) throws IOException {
		if (file == null || file.isEmpty()) {
			return null;
		}
 
		File targetDir = new File(uploadDir, subFolder);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
 
		String originalName = Path.of(file.getOriginalFilename()).getFileName().toString();
		String fileName = System.currentTimeMillis() + "_" + originalName;
 
		Path target = targetDir.toPath().resolve(fileName);
		Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
 
		// Value stored in the DB / referenced by <img src="${ctx}/image/...">
		return subFolder + "/" + fileName;
	}
}