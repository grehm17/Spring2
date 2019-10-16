package com.userinterface.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;

@Service
public class FileService {
    private Path rootLocation;
    @Autowired
    public void setRootLocation(@Value("${rootFolder}") String rootFolder) {
        this.rootLocation = Paths.get(rootFolder);
    }

    public Stream<Path> getFileList(String userFolder){
        Path userLocation = rootLocation.resolve(userFolder);
        try {
            return Files.walk(userLocation, 1)
                    .filter(path -> !path.equals(userLocation));
        }
        catch (IOException e) {
            throw new RuntimeException("\"Failed");
        }
    }

    public boolean deleteFile(String filePath){
        try {
            Path file = rootLocation.resolve(filePath);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveFile(MultipartFile file, String folder) {
        if (file.isEmpty()) {
            return false;
        }
        Path path = rootLocation.resolve(folder);
        path = path.resolve(file.getOriginalFilename());
        try {
            file.transferTo(path);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error! -> message = " + e.getMessage());
        }
    }

    public boolean createFolder(String folderName){
        Path file = rootLocation.resolve(folderName);
        try {
            Files.createDirectory(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
