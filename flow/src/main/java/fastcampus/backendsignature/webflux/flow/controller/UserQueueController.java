package fastcampus.backendsignature.webflux.flow.controller;

import fastcampus.backendsignature.webflux.flow.dto.AllowUserResponse;
import fastcampus.backendsignature.webflux.flow.dto.AllowedUserResponse;
import fastcampus.backendsignature.webflux.flow.dto.RegisterUserResponse;
import fastcampus.backendsignature.webflux.flow.dto.UserRankResponse;
import fastcampus.backendsignature.webflux.flow.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class UserQueueController {
    private final UserQueueService userQueueService;

    public static final String TOKEN_COOKIE_KEY_FORMAT = "user-queue-%s-token";

    @PostMapping("/test-data")
    public Mono<RegisterUserResponse> generateTestData(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "count") Long count
    ) {
        return Flux.range(1, count.intValue())
                .flatMap(i -> userQueueService.registerWaitQueue(queue, (long) i))
                .subscribeOn(Schedulers.boundedElastic())
                .reduce(0L, Long::sum)
                .map(RegisterUserResponse::new);
    }

    @PostMapping("")
    public Mono<RegisterUserResponse> registerUser(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "user_id") Long userId
    ) {
        return userQueueService.registerWaitQueue(queue, userId)
                .map(RegisterUserResponse::new);
    }

    @PostMapping("/allow")
    public Mono<AllowUserResponse> allowUser(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "count") Long count
    ) {
        return userQueueService.allowUser(queue, count)
                .map(AllowUserResponse::new);
    }

    // 진입 가능한 상태인지 조회
    @GetMapping("/allowed")
    public Mono<AllowedUserResponse> isAllowedUser(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "user_id") Long userId
    ) {
        return userQueueService.isAllowed(queue, userId)
                .map(AllowedUserResponse::new);
    }

    // 대기번호 조회
    @GetMapping("/rank")
    public Mono<UserRankResponse> getRank(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "user_id") Long userId
    ) {
        return userQueueService.getRank(queue, userId)
                .map(UserRankResponse::new);
    }

    @GetMapping("/touch")
    Mono<?> touch(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "user_id") Long userId,
            ServerWebExchange exchange
    ) {
        return Mono.defer(() -> userQueueService.generateToken(queue, userId))
                .map(token -> {
                    exchange.getResponse().addCookie(
                            ResponseCookie.from(TOKEN_COOKIE_KEY_FORMAT.formatted(queue), token)
                                    .maxAge(Duration.ofMinutes(1))
                                    .path("/")
                                    .build()
                    );
                    return token;
                });
    }
}
