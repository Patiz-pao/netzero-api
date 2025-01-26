package com.netzero.version.demo.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netzero.version.demo.Services.UsersService;
import com.netzero.version.demo.domain.FeedbackReq;
import com.netzero.version.demo.domain.FeedbackRes;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/feedback")
public class UsersController {

    @Autowired
    private final UsersService usersService;

    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> submitFeedback(
            @ModelAttribute FeedbackReq feedbackReq,
            @RequestParam("imageFile") MultipartFile imageFile) {

        log.info("Received FeedbackReq: {}", feedbackReq);
        log.info("Received ImageFile: {}", imageFile.getOriginalFilename());
        try {
            usersService.saveData(feedbackReq, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback submitted successfully.");
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
//
//    @GetMapping("/images")
//    public List<ResponseEntity<InputStreamResource>> getAllFeedbackWithImages() {
//        return usersService.getAllFeedbackWithDecodedImages();
//
//    }

    @GetMapping("/getAllFeedback")
    public List<FeedbackRes> getAllFeedbackWithImagesAsFiles() {
        return usersService.getAllFeedback();
    }
}