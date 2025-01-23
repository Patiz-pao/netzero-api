package com.netzero.version.demo.domain;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeedbackReq {
    private String username;
    private String email;
    private String phone;
    private String comment;

}
