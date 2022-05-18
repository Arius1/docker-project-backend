package com.example.springboot.Controllers;

import com.example.springboot.Services.ImageService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class ImgReceiverController {

    private ImageService imageService;

    public ImgReceiverController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/storePhoto")
    @ResponseBody
    public ResponseEntity<String> storePhoto
            (@RequestParam("file") MultipartFile file) {
        return new ResponseEntity(imageService.StorePhoto(file), HttpStatus.OK);
    }
    @GetMapping("/getLastPhoto")
    public ResponseEntity<InputStreamResource> getLastPhoto (){
        return imageService.GetPhoto();
    }

    @PostMapping("/sendResponse")
    @ResponseBody
    public ResponseEntity takeResponse
            (@RequestParam("response") String response) {
        imageService.StoreResponse(response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getResponse/")
    @ResponseBody
    public ResponseEntity<String> getResponse(@RequestParam("filename") String filename) {
        return new ResponseEntity(imageService.GetResponse(filename), HttpStatus.OK);
    }

    @GetMapping("/getTasksCount")
    public ResponseEntity<Integer> getTasksCount() {
        return new ResponseEntity(imageService.GetTasksCount(), HttpStatus.OK);
    }
}
