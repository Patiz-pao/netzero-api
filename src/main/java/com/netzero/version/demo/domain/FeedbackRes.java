package com.netzero.version.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeedbackRes {
    private String username;
    private String email;
    private String phone;
    private String comment;
    private String imagesBase64;
}
