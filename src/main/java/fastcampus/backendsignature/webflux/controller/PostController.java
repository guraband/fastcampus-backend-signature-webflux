package fastcampus.backendsignature.webflux.controller;

import fastcampus.backendsignature.webflux.dto.PostResponse;
import fastcampus.backendsignature.webflux.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public Mono<PostResponse> getPost(@PathVariable(name = "id") Long id) {
        return postService.getPost(id);
    }
}
