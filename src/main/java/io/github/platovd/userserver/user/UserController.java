package io.github.platovd.userserver.user;

import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getUser(userId));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid UserRequest request) {
        return ResponseEntity.ok(service.createUser(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @Valid UserRequest request) {
        service.updateUser(userId, request);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping(path = "/{userId}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> patchUser(@PathVariable String userId, @RequestBody JsonPatch patch) {
        service.patchUser(userId, patch);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/exists/{userId}")
    public ResponseEntity<Boolean> existsUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.existsById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable String userId) {
        service.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }
}
