package com.example.fruit.converter.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class ProblemDetailsFactory {

    private ProblemDetailsFactory() {}

    public static ProblemDetail of(HttpStatus status, String message, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, message);
        pd.setTitle(status.getReasonPhrase());

        if (request != null) {
            pd.setInstance(URI.create(request.getRequestURI()));
        }

        pd.setProperty("timestamp", OffsetDateTime.now(ZoneOffset.UTC));

        return pd;
    }
}