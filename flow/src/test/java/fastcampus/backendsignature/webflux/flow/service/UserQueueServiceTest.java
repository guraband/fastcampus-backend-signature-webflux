package fastcampus.backendsignature.webflux.flow.service;

import fastcampus.backendsignature.webflux.flow.EmbeddedRedis;
import fastcampus.backendsignature.webflux.flow.exception.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest
@Import(EmbeddedRedis.class)
@ActiveProfiles("test")
class UserQueueServiceTest {
    @Autowired
    private UserQueueService userQueueService;

    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @BeforeEach
    public void resetRedisData() {
        ReactiveRedisConnection connection = reactiveRedisTemplate.getConnectionFactory().getReactiveConnection();
        connection.serverCommands().flushAll().subscribe();
        System.out.println("# redis flushAll");
    }

    @Test
    @DisplayName("등록이 성공하면 순서를 리턴한다.")
    void registerUser() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 101L))
                .expectNext(1L)
                .verifyComplete();
        StepVerifier.create(userQueueService.registerWaitQueue("default", 102L))
                .expectNext(2L)
                .verifyComplete();
        StepVerifier.create(userQueueService.registerWaitQueue("default", 103L))
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    @DisplayName("중복해서 등록할 경우 오류가 발생한다.")
    void 중복_등록시_오류() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 101L))
                .expectNext(1L)
                .verifyComplete();
        StepVerifier.create(userQueueService.registerWaitQueue("default", 101L))
                .expectError(ApplicationException.class)
                .verify();

    }

    @Test
    @DisplayName("대기열의 사용자를 진입 가능한 상태로 전환한다.")
    void allowUser() {
        var queueName = "default";
        StepVerifier.create(
                        userQueueService.registerWaitQueue(queueName, 101L)
                                .then(userQueueService.registerWaitQueue(queueName, 102L))
                                .then(userQueueService.registerWaitQueue(queueName, 103L))
                                .then(userQueueService.allowUser(queueName, 2L))
                )
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    @DisplayName("대기열의 사용자를 모두 진입 가능한 상태로 전환한 뒤 새로 대기열이 추가된 상태 검증")
    void registUserAfterAllowUsers() {
        var queueName = "default";
        StepVerifier.create(
                        userQueueService.registerWaitQueue(queueName, 101L)
                                .then(userQueueService.registerWaitQueue(queueName, 102L))
                                .then(userQueueService.registerWaitQueue(queueName, 103L))
                                .then(userQueueService.allowUser(queueName, 3L))
                                .then(userQueueService.registerWaitQueue(queueName, 104L))
                )
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("진입 가능한 사용자인지 체크한다.")
    void isAllowedUser() {
        var queueName = "default";
        StepVerifier.create(
                        userQueueService.registerWaitQueue(queueName, 101L)
                                .then(userQueueService.registerWaitQueue(queueName, 102L))
                                .then(userQueueService.registerWaitQueue(queueName, 103L))
                )
                .expectNext(3L)
                .verifyComplete();

        StepVerifier.create(userQueueService.allowUser(queueName, 1L))
                .expectNext(1L)
                .verifyComplete();

        StepVerifier.create(userQueueService.isAllowed(queueName, 101L))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("남은 순서 조회 테스트")
    void getRank() {
        var queueName = "default";
        StepVerifier.create(
                        userQueueService.registerWaitQueue(queueName, 101L)
                                .then(userQueueService.registerWaitQueue(queueName, 102L))
                                .then(userQueueService.registerWaitQueue(queueName, 103L))
                                .then(userQueueService.getRank(queueName, 102L))
                )
                .expectNext(2L)
                .verifyComplete();
        StepVerifier.create(userQueueService.getRank(queueName, 200L)
                )
                .expectNext(-1L)
                .verifyComplete();
    }
}