package com.github.mpacala00.forum.pojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class HttpResponse {

    private String timeStamp;
    private int httpStatusCode;
    private String reasonPhrase; //reasonPhrase, such as CONTINUE (in case of status code 100)
    private String message;

    public HttpResponse(HttpStatus httpStatus, String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        timeStamp = formatter.format(now);

        this.httpStatusCode = httpStatus.value();
        this.reasonPhrase = httpStatus.getReasonPhrase();
        this.message = message;
    }
    
    public static ResponseEntity<HttpResponse> createResponseEntity(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus, message), httpStatus);
    }
}
