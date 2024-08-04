package fastcampus.backendsignature.webflux.example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Publisher {
    public Flux<Integer> startFlux() {
        return Flux.range(1, 5)
                .log();
    }

    public Mono<Integer> startMono() {
        return Mono.just(1)
                .log();
    }

    public Mono<?> startEmptyMono() {
        return Mono.empty().log();
    }

    public Mono<?> startMono2() {
        return Mono.error(new Exception("error!"))
                .log();
    }
}
