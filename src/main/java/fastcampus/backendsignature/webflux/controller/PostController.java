package fastcampus.backendsignature.webflux.controller;

import fastcampus.backendsignature.webflux.dto.PostResponse;
import fastcampus.backendsignature.webflux.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public Mono<PostResponse> getPost(@PathVariable(name = "id") Long id) {
        return postService.getPost(id);
    }

    @GetMapping("/search")
    public Flux<PostResponse> getPosts(@RequestParam(name = "ids") List<Long> ids) {
//        return postService.getPosts(ids);
        return postService.getPostsUsingParallel(ids);
    }
}
