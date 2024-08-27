package fastcampus.backendsignature.webflux.service;

import fastcampus.backendsignature.webflux.repository.User;
import fastcampus.backendsignature.webflux.repository.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {
    //    private final UserRepository userRepository;
    private final UserR2dbcRepository userRepository;
    private final ReactiveRedisTemplate<String, User> reactiveRedisTemplate;

    public Mono<User> create(String name, String email) {
        return userRepository.save(User.newUser(name, email));
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    private String getUserCacheKey(Long id) {
        return "users:%d" .formatted(id);
    }

    public Mono<User> findById(Long id) {
        return reactiveRedisTemplate.opsForValue()
                .get(getUserCacheKey(id))
                .switchIfEmpty(userRepository.findById(id)
                        .flatMap(u -> reactiveRedisTemplate.opsForValue()
                                .set(getUserCacheKey(id), u, Duration.ofSeconds(30))
                                .then(Mono.just(u)))
                );
    }

    public Flux<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Flux<User> findByNameOrderByIdDesc(String name) {
        return userRepository.findByNameOrderByIdDesc(name);
    }

    public Mono<User> update(Long id, String name, String email) {
        return userRepository.findById(id)
                .flatMap(user -> {
                    user.setName(name);
                    user.setEmail(email);
                    return userRepository.save(user);
                })
                .flatMap(user -> reactiveRedisTemplate.unlink(getUserCacheKey(id))
                        .then(Mono.just(user)));
    }

    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id)
                .then(reactiveRedisTemplate.unlink(getUserCacheKey(id)))
                .then(Mono.empty());
    }

    public Mono<Void> deleteByName(String name) {
        return userRepository.deleteByName(name);
    }
}
