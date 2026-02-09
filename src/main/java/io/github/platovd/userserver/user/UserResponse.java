package io.github.platovd.userserver.user;

import java.time.LocalDateTime;

public record UserResponse(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        UserStatus userStatus,
        LocalDateTime creationTime
) {
}
