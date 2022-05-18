package com.example.springboot.Services;

import com.example.springboot.Exceptions.StorageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class ImageService {

    private HashMap<String, String> scanResponses = new HashMap<>();
    private List<String> filenames = new ArrayList<>();
    private List<MultipartFile> photos = new ArrayList<>();

    public ResponseEntity<InputStreamResource> GetPhoto () {
        return SendJPEGAsResponseEntity(filenames.get(filenames.size()-1));
    }

    public String StorePhoto(MultipartFile file) {
        return storeJPEGtoList(file);
    }

    public void StoreResponse(String response) {
        scanResponses.put(filenames.get(filenames.size()-1), response);
        filenames.remove(filenames.get(filenames.size()-1));
        photos.remove(photos.get(photos.size()-1));
    }

    public String GetResponse(String filename) {
        return scanResponses.get(filename);
    }

    public int GetTasksCount() {
        return filenames.size();
    }

    private String storeJPEG(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream,
                        Paths.get("src","main","resources","ImagesToScan", filename), REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        filenames.add(filename);
        return filename;
    }

    private ResponseEntity<InputStreamResource> SendJPEGAsResponseEntity(String filename) {
        try {
            var imgFile = new ClassPathResource("ImagesToScan/" + filename);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(imgFile.getInputStream()));
        } catch (IOException e) {
            throw new StorageException("System Error with file" + filename, e);
        }
    }

    private String storeJPEGtoList(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        photos.add(file);
        filenames.add(filename);
        return filename;
    }

    private ResponseEntity<InputStreamResource> SendJPEGFromList(String filename) {
        try {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(photos.get(photos.size()-1).getInputStream()));
        } catch (IOException e) {
            throw new StorageException("System Error with file" + filename, e);
        }
    }
}
