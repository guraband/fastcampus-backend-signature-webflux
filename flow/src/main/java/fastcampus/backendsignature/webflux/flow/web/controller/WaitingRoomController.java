package fastcampus.backendsignature.webflux.flow.web.controller;

import fastcampus.backendsignature.webflux.flow.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class WaitingRoomController {
    private final UserQueueService userQueueService;

    @GetMapping("/waiting-room")
    public Mono<Rendering> waitingRoomPage(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "redirect_url") String redirect_url
    ) {
        return userQueueService.isAllowed(queue, userId)
                .filter(allowed -> allowed)
                .flatMap(i -> Mono.just(Rendering.redirectTo(redirect_url).build()))
                .switchIfEmpty(
                        userQueueService.registerWaitQueue(queue, userId)
                                .onErrorResume(ex -> userQueueService.getRank(queue, userId))
                                .map(rank -> Rendering.view("waiting-room.html")
                                        .modelAttribute("rank", rank)
                                        .modelAttribute("userId", userId)
                                        .modelAttribute("queue", queue)
                                        .build()
                                )
                );
    }
}
