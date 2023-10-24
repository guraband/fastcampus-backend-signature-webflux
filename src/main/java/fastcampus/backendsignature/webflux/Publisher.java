package fastcampus.backendsignature.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Publisher {
    public Flux<Integer> startFlux() {
        return Flux.range(1, 10)
                .log();
    }

    public Mono<Integer> startMono() {
        return Mono.just(1)
                .log();
    }
}
