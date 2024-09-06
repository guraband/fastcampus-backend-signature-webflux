package fastcampus.backendsignature.webflux.flow.service;

import fastcampus.backendsignature.webflux.flow.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserQueueService {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private final String USER_WAIT_KEY_FORMAT = "user:queue:%s:wait";
    private final String USER_WAIT_KEY_FOR_SCAN_FORMAT = "user:queue:*:wait";
    private final String USER_PROCEED_KEY_FORMAT = "user:queue:%s:proceed";

    private final String ALLOWED_USER_TOKEN_FORMAT = "user-queue-%s-%d";

    public Mono<Long> registerWaitQueue(
            final String queue, final Long userId) {

        log.info("# queue : {}, userId : {}", queue, userId);

        var member = userId.toString();
        final var key = USER_WAIT_KEY_FORMAT.formatted(queue);
        var unitTimeStamp = Instant.now().getEpochSecond();
        return reactiveRedisTemplate.opsForZSet().add(key, member, unitTimeStamp)
                .filter(i -> i)
                .switchIfEmpty(Mono.error(ErrorCode.QUEUE_ALREADY_REGISTERED_USER.build()))
                .flatMap(i -> reactiveRedisTemplate.opsForZSet().rank(key, member))
                .map(i -> i >= 0 ? i + 1 : i)
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

    public void scheduleAllowUser() {
        var maxAllowUserCount = 3L;
        reactiveRedisTemplate.scan(ScanOptions.scanOptions()
                        .match(USER_WAIT_KEY_FOR_SCAN_FORMAT)
                        .count(100)
                        .build())
                .map(key -> key.split(":")[2])
                .flatMap(queue -> allowUser(queue, maxAllowUserCount).map(allowed -> Tuples.of(queue, allowed)))
                .doOnNext(tuple -> log.info("[{}] 허용 요청 수 : {}, 허용 수 : {}", tuple.getT1(), maxAllowUserCount, tuple.getT2()))
                .subscribe();
    }

    // 진입 가능한 상태인지 조회
    public Mono<Boolean> isAllowed(final String queue, final Long userId) {
        final var proceedKey = USER_PROCEED_KEY_FORMAT.formatted(queue);
        return reactiveRedisTemplate.opsForZSet().rank(proceedKey, userId.toString())
                .defaultIfEmpty(-1L)
                .map(i -> i >= 0);
    }

    public Mono<Boolean> isAllowedByToken(final String queue,
                                          final Long userId,
                                          final String token
    ) {
        return this.generateToken(queue, userId)
                .filter(compareToken -> compareToken.equals(token))
                .map(i -> true)
                .defaultIfEmpty(false);
    }

    // 진입 가능해진 사용자에게 토큰 발급
    public Mono<String> generateToken(final String queue, final Long userId) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            var input = ALLOWED_USER_TOKEN_FORMAT.formatted(queue, userId);
            var encodedHash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b));
            }
            return Mono.just(hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<Long> getRank(final String queue, final Long userId) {
        final var waitKey = USER_WAIT_KEY_FORMAT.formatted(queue);
        return reactiveRedisTemplate.opsForZSet().rank(waitKey, userId.toString())
                .defaultIfEmpty(-1L)
                .map(rank -> rank > -1 ? rank + 1 : rank);
    }
}
