package fastcampus.backendsignature.webflux.flow.service;

import fastcampus.backendsignature.webflux.flow.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserQueueService {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private final String USER_WAIT_KEY_FORMAT = "user:queue:%s:wait";
    private final String USER_PROCEED_KEY_FORMAT = "user:queue:%s:proceed";

    public Mono<Long> registerWaitQueue(
            final String queue, final Long userId) {
        var member = userId.toString();
        final var key = USER_WAIT_KEY_FORMAT.formatted(queue);
        var unitTimeStamp = Instant.now().getEpochSecond();
        return reactiveRedisTemplate.opsForZSet().add(key, member, unitTimeStamp)
                .filter(i -> i)
                .switchIfEmpty(Mono.error(ErrorCode.QUEUE_ALREADY_REGISTERED_USER.build()))
                .flatMap(i -> reactiveRedisTemplate.opsForZSet().rank(key, member))
                ;
    }

    public Mono<Long> allowUser(
            final String queue, final Long count) {
        final var waitKey = USER_WAIT_KEY_FORMAT.formatted(queue);
        final var proceedKey = USER_PROCEED_KEY_FORMAT.formatted(queue);
        var unixTimeStamp = Instant.now().getEpochSecond();
        return reactiveRedisTemplate.opsForZSet().popMin(waitKey, count)
                .flatMap(u -> reactiveRedisTemplate.opsForZSet().add(proceedKey, Objects.requireNonNull(u.getValue()), unixTimeStamp))
                .count()
                ;
    }

    // 진입 가능한 상태인지 조회
    public Mono<Boolean> isAllowed(final String queue, final Long userId) {
        final var proceedKey = USER_PROCEED_KEY_FORMAT.formatted(queue);
        return reactiveRedisTemplate.opsForZSet().rank(proceedKey, userId.toString())
                .defaultIfEmpty(-1L)
                .map(i -> i >= 0);
    }
}
