package fastcampus.backendsignature.webflux.controller;

import fastcampus.backendsignature.webflux.dto.PostCreateRequest;
import fastcampus.backendsignature.webflux.dto.PostResponse;
import fastcampus.backendsignature.webflux.service.PostServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/posts")
public class PostControllerV2 {
    private final PostServiceV2 postService;

    @PostMapping("")
    public Mono<PostResponse> createUser(@RequestBody PostCreateRequest request) {
        return postService.create(request.getUserId(), request.getTitle(), request.getContents());
    }

    @GetMapping("")
    public Flux<PostResponse> findAllUsers() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostResponse>> getPost(@PathVariable(name = "id") Long id) {
        return postService.findById(id)
                .map(response -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deletePost(@PathVariable(name = "id") Long id) {
        return postService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
