package fastcampus.backendsignature.webflux.flow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserQueueService {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private final String USER_QUEUE_KEY_FORMAT = "user:queue:%s:wait";

    public Mono<Long> registerWaitQueue(
            final String queue, final Long userId) {
        var member = userId.toString();
        final var key = USER_QUEUE_KEY_FORMAT.formatted(queue);
        var unitTimeStamp = Instant.now().getEpochSecond();
        return reactiveRedisTemplate.opsForZSet().add(key, member, unitTimeStamp)
                .flatMap(i -> reactiveRedisTemplate.opsForZSet().rank(key, member))
                ;
    }
}
