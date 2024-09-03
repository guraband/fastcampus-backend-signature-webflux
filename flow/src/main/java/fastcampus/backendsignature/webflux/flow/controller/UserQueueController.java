package fastcampus.backendsignature.webflux.flow.controller;

import fastcampus.backendsignature.webflux.flow.dto.AllowUserResponse;
import fastcampus.backendsignature.webflux.flow.dto.AllowedUserResponse;
import fastcampus.backendsignature.webflux.flow.dto.RegisterUserResponse;
import fastcampus.backendsignature.webflux.flow.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class UserQueueController {
    private final UserQueueService userQueueService;

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
                .map(AllowedUserResponse::new
                );
    }
}
