package com.tus.trafficsimulator.models;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.tus.trafficsimulator.models.enums.ActivationAction;

/**
 * Represents a request to manage the activation of a network.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivationManagementRequest {

    @NotNull
    private ActivationAction action;
}
