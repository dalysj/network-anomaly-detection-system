package com.tus.trafficsimulator.exceptions.handlers;

import com.tus.trafficsimulator.exceptions.TrafficSimulatorException;
import com.tus.trafficsimulator.models.ProblemDetails;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles exceptions thrown by the Traffic Simulator application.
 */
@RestControllerAdvice
public class GlobalTrafficSimulatorExceptionHandler {

    /**
     * Handles TrafficSimulatorException exceptions.
     * 
     * @param exception The exception to handle.
     * @return A ResponseEntity containing the ProblemDetails.
     */
    @ExceptionHandler(TrafficSimulatorException.class)
    public ResponseEntity<ProblemDetails> handleTrafficSimulatorException(final TrafficSimulatorException exception) {
        final String detail = this.getErrorDetail(exception);
        final ProblemDetails problemDetails = ProblemDetails.builder()
                .title(exception.getTrafficSimulatorError().getTitle())
                .status(exception.getHttpStatus().value())
                .detail(detail)
                .build();
        return ResponseEntity.status(exception.getHttpStatus()).body(problemDetails);
    }

    private String getErrorDetail(final TrafficSimulatorException exception) {
        if (exception.getErrorDetailArgs() == null) {
            return exception.getTrafficSimulatorError().getDetail();
        }

        return String.format(exception.getTrafficSimulatorError().getDetail(),
                (Object[]) exception.getErrorDetailArgs());
    }

}
