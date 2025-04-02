package com.tus.trafficsimulator.exceptions;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.http.HttpStatus;

import com.tus.trafficsimulator.exceptions.enums.TrafficSimulatorError;

/**
 * Represents an exception that occurred in the Traffic Simulator application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrafficSimulatorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;

    private TrafficSimulatorError trafficSimulatorError;

    private String[] errorDetailArgs;

    /**
     * Constructs a new TrafficSimulatorException with the provided HTTP status,
     * error, and error detail arguments.
     *
     * @param httpStatus            The HTTP status of the exception.
     * @param trafficSimulatorError The error that occurred.
     */
    public TrafficSimulatorException(final HttpStatus httpStatus, final TrafficSimulatorError trafficSimulatorError) {
        super();
        this.httpStatus = httpStatus;
        this.trafficSimulatorError = trafficSimulatorError;
    }
}
