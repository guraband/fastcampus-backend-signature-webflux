package fastcampus.backendsignature.webflux.service;

import fastcampus.backendsignature.webflux.client.PostClient;
import fastcampus.backendsignature.webflux.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostClient client;

    public Mono<PostResponse> getPost(Long id) {
        return client.getPost(id);
    }
}
