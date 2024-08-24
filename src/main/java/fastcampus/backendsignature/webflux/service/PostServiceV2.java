package fastcampus.backendsignature.webflux.service;

import fastcampus.backendsignature.webflux.dto.PostResponse;
import fastcampus.backendsignature.webflux.repository.Post;
import fastcampus.backendsignature.webflux.repository.PostR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostServiceV2 {
    private final PostR2dbcRepository postR2dbcRepository;

    public Mono<PostResponse> create(Long userId, String title, String contents) {
        return postR2dbcRepository.save(Post.of(userId, title, contents))
                .map(PostResponse::new);
    }

    public Flux<PostResponse> findAll() {
        return postR2dbcRepository.findAll()
                .map(PostResponse::new);
    }

    public Mono<PostResponse> findById(Long id) {
        return postR2dbcRepository.findById(id)
                .map(PostResponse::new);
    }

    public Mono<Void> delete(Long id) {
        return postR2dbcRepository.deleteById(id);
    }
}
