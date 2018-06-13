package com.example.demo.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}
			Path filePath = this.rootLocation.resolve(file.getOriginalFilename());
			if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
				delete(file.getOriginalFilename());
			}
			Files.copy(file.getInputStream(), filePath);
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
					.filter(path -> !path.equals(this.rootLocation) && path.toFile().isFile())
					.map(path -> this.rootLocation.relativize(path));
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	@PostConstruct
	public void init() {
		try {
			if (!Files.exists(rootLocation, LinkOption.NOFOLLOW_LINKS)) {
				Files.createDirectory(rootLocation);
			}
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public void delete(String fileName) {

		try {
			Path file = load(fileName);
			Files.delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Path createExportDir() {
		String dirName = "exports";
		Path filePath = this.rootLocation.resolve(dirName);
		try {

			if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
				FileUtils.deleteDirectory(filePath.toFile());
			}
			Files.createDirectory(filePath,
					PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-x---")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	@Override
	public void storeFileIntoExprotDir(String fileName, InputStream input) {
		String dirName = "exports";
		Path filePath = this.rootLocation.resolve(dirName).resolve(fileName);
		try {
			Files.deleteIfExists(filePath);
			Files.copy(input, filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void pack(Path sourceDirPath, Path zipFilePath) throws IOException {
		Path p = Files.createFile(zipFilePath);
		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
			Files.walk(sourceDirPath).filter(path -> !Files.isDirectory(path)).forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
				try {
					zs.putNextEntry(zipEntry);
					Files.copy(path, zs);
					zs.closeEntry();
				} catch (IOException e) {
					System.err.println(e);
				}
			});
		}
	}

	@Override
	public String zipExportDir() {
		String zipFileName = "exports.zip";
		Path zipFilePath = this.rootLocation.resolve(zipFileName);
		String dirName = "exports";
		Path dirPath = this.rootLocation.resolve(dirName);
		try {
			Files.deleteIfExists(zipFilePath);
			pack(dirPath, zipFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zipFileName;
	}
}
