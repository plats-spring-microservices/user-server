package io.github.platovd.userserver.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;

@Component
public class UserMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserStatus(),
                user.getCreationDate()
        );
    }

    public User toEntity(UserRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .provider(request.provider())
                .providerUserId(request.providerUserId())
                .userStatus(request.userStatus())
                .build();
    }

    public User toUserWithId(String userId, UserRequest request) {
        User user = toEntity(request);
        user.setId(userId);
        return user;
    }

    public void updateFromRequest(User user, UserRequest request) {
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUserStatus(request.userStatus());
    }

    public UserRequest toRequest(User user) {
        return new UserRequest(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProvider(),
                user.getProviderUserId(),
                user.getUserStatus()
        );
    }

    public UserRequest patchToRequest(JsonPatch patch, UserRequest request) throws JsonPatchException, JsonProcessingException {
        JsonNode target = objectMapper.valueToTree(request);
        JsonNode patched = patch.apply(target);
        return objectMapper.treeToValue(patched, UserRequest.class);
    }

    public User toEntityFromMessage(UserCreationMessage message) {
        return User.builder()
                .id(message.id())
                .username(message.username())
                .email(message.email())
                .firstName(message.firstName())
                .lastName(message.lastName())
                .provider(AuthProvider.valueOf(message.provider()))
                .providerUserId(message.providerUserId())
                .build();
    }
}
