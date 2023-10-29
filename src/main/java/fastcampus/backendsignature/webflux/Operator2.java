package fastcampus.backendsignature.webflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class Operator2 {
    // concatMap
    public Flux<Integer> fluxConcatMap() {
        return Flux.range(1, 10)
                .concatMap(i -> Flux.range(i * 10, 10)
                        .delayElements(Duration.ofMillis(50)))
                .log();
    }

    // flatMapMany : mono to flux
    public Flux<Integer> fluxMapMany() {
        return Mono.just(10)
                .flatMapMany(i -> Flux.range(1, i))
                .log();
    }

    // defaultIfEmpty
    public Mono<Integer> defaultIfEmpty() {
        return Mono.just(100)
                .filter(i -> i > 100)
                .defaultIfEmpty(30)
                .log();
    }

    // switchIfEmpty
    public Mono<Integer> switchIfEmpty() {
        return Mono.just(100)
                .filter(i -> i > 100)
                .switchIfEmpty(Mono.just(30).map(i -> i * 2))
                .log();
    }

    public Mono<Integer> switchIfEmpty2() {
        return Mono.just(100)
                .filter(i -> i > 100)
                .switchIfEmpty(Mono.error(new Exception("empty value")))
                .log();
    }

    public Flux<Integer> fluxMerge() {
        return Flux.merge(Flux.range(1, 3), Flux.fromIterable(List.of(4, 5)))
                .log();
    }

    public Flux<Integer> monoMerge() {
        return Mono.just(1)
                .mergeWith(Mono.just(2))
                .log();
    }

    public Flux<String> zip() {
        return Flux.zip(Flux.just("a", "b", "c", "d"),
                        Flux.range(1, 3))
                .map(o -> o.getT1() + o.getT2())
                .log();
    }
}
