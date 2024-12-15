package com.netzero.version.demo.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.netzero.version.demo.constants.Constants.RequestHeader.REQUEST_DATE;

public class ResponseHelper {

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private ResponseHelper(){
        throw new IllegalArgumentException("Utility class");
    }

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String DATA = "data";

    public static ResponseEntity<Object> response(HttpStatus httpStatus, Object object){
        HttpHeaders httpHeaders = new HttpHeaders();
        if (RequestContextHolder.getRequestAttributes() != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            httpHeaders.add(REQUEST_DATE, request.getHeader(REQUEST_DATE));
        }

        httpHeaders.add(REQUEST_DATE, LocalDateTime.now().format(timeFormatter));
        return ResponseEntity.status(httpStatus).headers(httpHeaders).body(object);
    }

    public static ResponseEntity<Object> success(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put(MESSAGE, message);
        data.put(STATUS, true);
        return response(HttpStatus.OK, data);
    }

    public static ResponseEntity<Object> successWithData(String message, Object obj) {
        Map<String, Object> data = new HashMap<>();
        data.put(MESSAGE, message);
        data.put(STATUS, true);
        data.putAll(objectMapper.convertValue(obj, Map.class));
        return response(HttpStatus.OK, data);
    }

    public static ResponseEntity<Object> successWithList(String message, Object obj) {
        Map<String, Object> data = new HashMap<>();
        data.put(MESSAGE, message);
        data.put(STATUS, true);
        data.put(DATA, obj);
        return response(HttpStatus.OK, data);
    }

    public static ResponseEntity<Object> successPage(String message, Object obj,long totalPage, long total) {
        Map<String, Object> data = new HashMap<>();
        data.put(MESSAGE, message);
        data.put(STATUS, true);
        data.put("totalPage", totalPage);
        data.put("total", total);
        data.put(DATA, obj);
        return response(HttpStatus.OK, data);
    }

    public static<T> ResponseEntity<GenericResponse<T>> responseWithMessage(HttpStatus status, String message){
        GenericResponse<T> response = new GenericResponse<>(status, message);
        return ResponseEntity.status(status).body(response);
    }

    public static<T> ResponseEntity<GenericResponse<T>> responseWithData(HttpStatus status, String message,T data){
        GenericResponse<T> response = new GenericResponse<>(status, message, data);
        return ResponseEntity.status(status).body(response);

    }



}
