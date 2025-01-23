package com.netzero.version.demo.Services;

import com.netzero.version.demo.Entity.FeedbackEntity;
import com.netzero.version.demo.Repository.FeedbackRepo;
import com.netzero.version.demo.domain.FeedbackReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

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
}
