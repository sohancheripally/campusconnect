
package com.campusconnect.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectRequest(
        @NotBlank(message = "Subject name is required")
        String name
) {
}
