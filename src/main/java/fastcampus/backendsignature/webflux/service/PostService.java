package fastcampus.backendsignature.webflux.service;

import fastcampus.backendsignature.webflux.client.PostClient;
import fastcampus.backendsignature.webflux.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostClient client;

    public Mono<PostResponse> getPost(Long id) {
        System.out.printf("# id : %d%n", id);
        return client.getPost(id)
                .onErrorResume(error -> Mono.empty());
    }

    public Flux<PostResponse> getPosts(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(this::getPost)
                .log();
    }

    public Flux<PostResponse> getPostsUsingParallel(List<Long> ids) {
        return Flux.fromIterable(ids)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(this::getPost)
                .log()
                .sequential();
    }
}
