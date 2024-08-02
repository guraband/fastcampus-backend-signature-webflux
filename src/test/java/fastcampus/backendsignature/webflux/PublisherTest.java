package fastcampus.backendsignature.webflux;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class PublisherTest {

    private final Publisher publisher = new Publisher();

    @Test
    void startFlux() {
        StepVerifier.create(publisher.startFlux())
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    void startMono() {
        StepVerifier.create(publisher.startMono())
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    void startMono2() {
        StepVerifier.create(publisher.startMono2())
                .expectError(Exception.class)
                .verify();
    }
}