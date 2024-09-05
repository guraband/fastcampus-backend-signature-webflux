package fastcampus.backendsignature.webflux.flow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class QueueScheduler {
    private final UserQueueService userQueueService;

    @Scheduled(initialDelay = 5_000, fixedDelay = 3_000L)
    public void allowUser() {
        userQueueService.scheduleAllowUser();
    }
}
