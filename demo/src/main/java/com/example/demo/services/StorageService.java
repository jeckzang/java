package com.example.demo.services;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	void store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void delete(String fileName);

	void deleteAll();

	Path createExportDir();

	void storeFileIntoExprotDir(String fileName, InputStream input);

	String zipExportDir();

}