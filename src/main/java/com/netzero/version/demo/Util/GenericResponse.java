package com.netzero.version.demo.Util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public GenericResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public GenericResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


}
