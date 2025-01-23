package com.netzero.version.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "nz_feedback")
public class FeedbackEntity {
    @Id
    @Column(name = "feedback_id", nullable = false)
    private String feedbackId;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "images_base64", columnDefinition = "TEXT")
    private String imagesBase64;

    @Column(name = "comment")
    private String comment;

}
