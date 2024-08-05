package fastcampus.backendsignature.webflux.controller;

import fastcampus.backendsignature.webflux.dto.UserCreateRequest;
import fastcampus.backendsignature.webflux.dto.UserResponse;
import fastcampus.backendsignature.webflux.dto.UserUpdateRequest;
import fastcampus.backendsignature.webflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public Mono<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        return userService.create(request.getName(), request.getEmail())
                .map(UserResponse::of);
    }

    @GetMapping("")
    public Flux<UserResponse> findAllUsers() {
        return userService.findAll()
                .map(UserResponse::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findUser(@PathVariable("id") Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserResponse.of(user)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteUser(@PathVariable("id") Long id) {
        return userService.delete(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PutMapping("")
    public Mono<ResponseEntity<UserResponse>> updateUser(@RequestBody UserUpdateRequest request) {
        return userService.update(request.getId(), request.getName(), request.getEmail())
                .map(user -> ResponseEntity.ok(UserResponse.of(user)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
