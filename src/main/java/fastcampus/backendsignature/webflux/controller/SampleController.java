package fastcampus.backendsignature.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("")
public class SampleController {
    @GetMapping("/sample/hello")
    public Mono<String> getHello() {
        return Mono.just("Hello RestController endpoint!");
    }
}
