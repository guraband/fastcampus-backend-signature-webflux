package fastcampus.backendsignature.webflux.example;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class Operator1Test {

    private Operator1 operator = new Operator1();

    @Test
    void fluxMap() {
        StepVerifier.create(operator.fluxMap())
                .expectNext(2, 4, 6, 8, 10)
                .verifyComplete();
    }

    @Test
    void fluxFilter() {
        StepVerifier.create(operator.fluxFilter())
                .expectNext(6, 7, 8, 9, 10)
                .verifyComplete();
    }

    @Test
    void fluxTake() {
        StepVerifier.create(operator.fluxTake())
                .expectNext(6, 7, 8)
                .verifyComplete();
    }

    @Test
    void fluxFlatMap() {
        StepVerifier.create(operator.fluxFlatMap())
                .expectNextCount(100)
                .verifyComplete();
    }

    @Test
    void fluxFlatMap2() {
        StepVerifier.create(operator.fluxFlatMap2())
                .expectNextCount(81)
                .verifyComplete();
    }
}