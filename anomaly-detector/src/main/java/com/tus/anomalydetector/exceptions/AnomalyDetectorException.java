package com.tus.anomalydetector.exceptions;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.http.HttpStatus;

import com.tus.anomalydetector.exceptions.enums.AnomalyDetectorError;

/**
 * Represents an exception that occurred in the Anomaly Detector application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnomalyDetectorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;

    private AnomalyDetectorError anomalyDetectorError;

    private String[] errorDetailArgs;

    /**
     * Constructs a new AnomalyDetectorException with the provided HTTP status,
     * error, and error detail arguments.
     *
     * @param httpStatus           The HTTP status of the exception.
     * @param anomalyDetectorError The error that occurred.
     */
    public AnomalyDetectorException(final HttpStatus httpStatus, final AnomalyDetectorError anomalyDetectorError) {
        super();
        this.httpStatus = httpStatus;
        this.anomalyDetectorError = anomalyDetectorError;
    }
}
