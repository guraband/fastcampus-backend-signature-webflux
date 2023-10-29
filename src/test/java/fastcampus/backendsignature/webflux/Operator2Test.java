package fastcampus.backendsignature.webflux;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class Operator2Test {
    private Operator2 operator = new Operator2();

    @Test
    void fluxConcatMap() {
        StepVerifier.create(operator.fluxConcatMap())
                .expectNextCount(100)
                .verifyComplete();
    }

    @Test
    void fluxMapMany() {
        StepVerifier.create(operator.fluxMapMany())
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .verifyComplete();
    }

    @Test
    void defaultIfEmpty() {
        StepVerifier.create(operator.defaultIfEmpty())
                .expectNext(30)
                .verifyComplete();
    }

    @Test
    void switchIfEmpty() {
        StepVerifier.create(operator.switchIfEmpty())
                .expectNext(60)
                .verifyComplete();
    }

    @Test
    void switchIfEmpty2() {
        StepVerifier.create(operator.switchIfEmpty2())
                .expectError()
                .verify();
    }

    @Test
    void fluxMerge() {
        StepVerifier.create(operator.fluxMerge())
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    void monoMerge() {
        StepVerifier.create(operator.monoMerge())
                .expectNext(1, 2)
                .verifyComplete();
    }

    @Test
    void zip() {
        StepVerifier.create(operator.zip())
                .expectNext("a1", "b2", "c3")
                .verifyComplete();
    }
}