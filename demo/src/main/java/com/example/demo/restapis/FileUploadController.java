package com.example.demo.restapis;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.StorageFileNotFoundException;
import com.example.demo.services.StorageService;

@RestController
public class FileUploadController {

	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@RequestMapping(value = "/files", method = RequestMethod.GET)
	public List<String> listUploadedFiles() throws IOException {
		return storageService.loadAll().map(path -> path.toFile().getName()).collect(Collectors.toList());
	}

	@RequestMapping(value = "/files/{fileName:.+}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
		Resource file = storageService.loadAsResource(fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@RequestMapping(value = "/files/{fileName:.+}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Resource> deleteFile(@PathVariable String fileName) {
		storageService.delete(fileName);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/files", consumes = "multipart/form-data")
	public void handleFileUpload(@RequestParam("record") MultipartFile file) {
		storageService.store(file);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}