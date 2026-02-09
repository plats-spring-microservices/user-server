package io.github.platovd.userserver.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserRequest(
        @NotBlank(message = "Username is required")
        String username,
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "First name can't be blank")
        String firstName,
        @NotBlank(message = "Last name can't be blank")
        String lastName,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @NotNull(message = "Auth provider should be set")
        AuthProvider provider,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @NotBlank(message = "Provider user id required")
        String providerUserId,
        UserStatus userStatus
) {
}
