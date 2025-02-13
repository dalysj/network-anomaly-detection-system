package com.tus.trafficsimulator.exceptions;

import com.tus.trafficsimulator.exceptions.enums.TrafficSimulatorError;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.http.HttpStatus;

/**
 * Represents an exception that occurred in the Traffic Simulator application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrafficSimulatorException extends RuntimeException {

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
     * @param errorDetailArgs       The arguments to use when formatting the error
     *                              detail.
     */
    public TrafficSimulatorException(final HttpStatus httpStatus, final TrafficSimulatorError trafficSimulatorError) {
        super();
        this.httpStatus = httpStatus;
        this.trafficSimulatorError = trafficSimulatorError;
    }
}
