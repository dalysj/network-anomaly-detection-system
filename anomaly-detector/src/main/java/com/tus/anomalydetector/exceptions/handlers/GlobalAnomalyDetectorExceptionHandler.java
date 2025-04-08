package com.tus.anomalydetector.exceptions.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tus.anomalydetector.exceptions.AnomalyDetectorException;
import com.tus.anomalydetector.models.ProblemDetails;

/**
 * Handles exceptions thrown by the Anomaly Detector application.
 */
@RestControllerAdvice
public class GlobalAnomalyDetectorExceptionHandler {

    /**
     * Handles AnomalyDetectorException exceptions.
     *
     * @param exception The exception to handle.
     * @return A ResponseEntity containing the ProblemDetails.
     */
    @ExceptionHandler(AnomalyDetectorException.class)
    public ResponseEntity<ProblemDetails> handleAnomalyDetectorException(final AnomalyDetectorException exception) {
        final String detail = this.getErrorDetail(exception);
        final ProblemDetails problemDetails = ProblemDetails.builder()
                .title(exception.getAnomalyDetectorError().getTitle())
                .status(exception.getHttpStatus().value())
                .detail(detail)
                .build();
        return ResponseEntity.status(exception.getHttpStatus()).body(problemDetails);
    }

    private String getErrorDetail(final AnomalyDetectorException exception) {
        if (exception.getErrorDetailArgs() == null) {
            return exception.getAnomalyDetectorError().getDetail();
        }

        return String.format(exception.getAnomalyDetectorError().getDetail(),
                (Object[]) exception.getErrorDetailArgs());
    }
}
