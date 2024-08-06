package fastcampus.backendsignature.webflux.controller;

import fastcampus.backendsignature.webflux.dto.UserCreateRequest;
import fastcampus.backendsignature.webflux.dto.UserResponse;
import fastcampus.backendsignature.webflux.dto.UserUpdateRequest;
import fastcampus.backendsignature.webflux.repository.User;
import fastcampus.backendsignature.webflux.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
@AutoConfigureWebTestClient
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    void createUser() {
        when(userService.create("페페", "pepe@gmail.com"))
                .thenReturn(
                        Mono.just(User.newUser("페페", "pepe@gmail.com"))
                );

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateRequest("페페", "pepe@gmail.com"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("페페", res.getName());
                    assertEquals("pepe@gmail.com", res.getEmail());
                });
    }

    @Test
    void findAllUsers() {
        when(userService.findAll()).thenReturn(
                Flux.just(
                        User.newUser("페페", "pepe@gmail.com"),
                        User.newUser("페페2", "pepe2@gmail.com")
                )
        );

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(UserResponse.class)
                .hasSize(2);
    }

    @Test
    void findUser() {
        when(userService.findById(1L))
                .thenReturn(
                        Mono.just(User.newUser("페페", "pepe@gmail.com"))
                );

        webTestClient.get().uri("/users/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("페페", res.getName());
                    assertEquals("pepe@gmail.com", res.getEmail());
                });
    }

    @Test
    void deleteUser() {
        when(userService.delete(1L))
                .thenReturn(
                        Mono.just(1)
                );

        webTestClient.delete().uri("/users/1")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void updateUser() {
        when(userService.update(1L, "페페", "pepe@gmail.com"))
                .thenReturn(
                        Mono.just(User.newUser("페페", "pepe@gmail.com"))
                );

        webTestClient.put().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdateRequest(1L, "페페", "pepe@gmail.com"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("페페", res.getName());
                    assertEquals("pepe@gmail.com", res.getEmail());
                });
    }
}