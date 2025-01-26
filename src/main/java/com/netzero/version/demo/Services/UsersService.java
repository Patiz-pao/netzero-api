package com.netzero.version.demo.Services;

import com.netzero.version.demo.Entity.FeedbackEntity;
import com.netzero.version.demo.Repository.FeedbackRepo;
import com.netzero.version.demo.domain.FeedbackReq;
import com.netzero.version.demo.domain.FeedbackRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsersService {

    private final FeedbackRepo feedbackRepo;

    public UsersService(FeedbackRepo feedbackRepo){
        this.feedbackRepo = feedbackRepo;
    }

    public void saveData(FeedbackReq req, MultipartFile imageFile) throws IOException {

        log.info("Request Data: {}", req);
        log.info("Image File: {}", imageFile.getOriginalFilename());

        FeedbackEntity feedback = new FeedbackEntity();
        //General information
        feedback.setFeedbackId(UUID.randomUUID().toString());
        feedback.setUsername(req.getUsername());
        feedback.setEmail(req.getEmail());
        feedback.setPhone(req.getPhone());
        feedback.setComment(req.getComment());

        log.info("Feedback Entity: {}", feedback);

        //image encode base64
        String base64Image = convertToBase64(imageFile);
        feedback.setImagesBase64(base64Image);
        //save to database
        feedbackRepo.save(feedback);
        log.info("Data save success.");
    }

    //encode image file
    private String convertToBase64(MultipartFile file) throws IOException {
        try {

            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("file is empty.");
            }
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("File size max limit 5MB");
            }

            byte[] fileBytes = file.getBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        }catch (IOException e){
            log.error("Error reading file: {}", e.getMessage());
            throw new IOException("Failed to process the file.", e);
        }
    }

//    public List<ResponseEntity<InputStreamResource>> getAllFeedbackWithDecodedImages() {
//        List<FeedbackEntity> feedbackEntities = feedbackRepo.findAll();
//
//        return feedbackEntities.stream().map(entity -> {
//            byte[] imageBytes = Base64.getDecoder().decode(entity.getImagesBase64());
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
//            InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + entity.getUsername() + ".jpeg");
//            headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");
//
//            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
//        }).collect(Collectors.toList());
//    }

    public List<FeedbackRes> getAllFeedback() {
        List<FeedbackEntity> feedbackEntities = feedbackRepo.findAll();
        return feedbackEntities.stream().map(entity -> {
            FeedbackRes response = new FeedbackRes();
            response.setUsername(entity.getUsername());
            response.setEmail(entity.getEmail());
            response.setPhone(entity.getPhone());
            response.setComment(entity.getComment());

            // Decode Base64 เป็น byte array และแปลงเป็น URL สำหรับ Response
            byte[] imageBytes = Base64.getDecoder().decode(entity.getImagesBase64());
            String imageBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);

            response.setImagesBase64(imageBase64);
            return response;
        }).collect(Collectors.toList());
    }
}
