package io.github.platovd.userserver.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.github.platovd.userserver.exception.PatchException;
import io.github.platovd.userserver.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final Validator validator;

    public UserResponse getUser(String userId) {
        return mapper.toResponse(repository.findById(userId).orElseThrow(() -> new UserNotFoundException("No user with id=" + userId)));
    }

    public String createUserFromMessage(UserCreationMessage message) {
        User user = mapper.toEntityFromMessage(message);
        repository.save(user);
        return user.getId();
    }

    public String createUser(@Valid UserRequest request) {
        User user = mapper.toEntity(request);
        repository.save(user);
        return user.getId();
    }

    public void updateUser(String userId, @Valid UserRequest request) {
        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("No user with id=" + userId));
        mergeUser(user, request);
    }

    private void mergeUser(User user, UserRequest request) {
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUserStatus(request.userStatus());
        repository.save(user);
    }

    public void patchUser(String userId, JsonPatch patch) {
        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("No user with id=" + userId));
        UserRequest request = mapper.toRequest(user);
        try {
            UserRequest effectedRequest = mapper.patchToRequest(patch, request);
            Set<ConstraintViolation<UserRequest>> violations = validator.validate(effectedRequest);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            mapper.updateFromRequest(user, effectedRequest);
            repository.save(user);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new PatchException("Patch unsuccessful");
        }
    }

    public boolean existsById(String userId) {
        return repository.existsById(userId);
    }

    protected boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public void deleteUser(String userId) {
        repository.deleteById(userId);
    }
}
