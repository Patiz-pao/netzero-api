package com.netzero.version.demo.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netzero.version.demo.Services.UsersService;
import com.netzero.version.demo.domain.FeedbackReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/feedback")
public class UsersController {

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
}
