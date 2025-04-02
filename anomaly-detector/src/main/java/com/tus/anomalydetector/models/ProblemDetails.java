package com.tus.anomalydetector.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the details of a problem that occurred.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProblemDetails {

    private String title;

    private int status;

    private String detail;
}
