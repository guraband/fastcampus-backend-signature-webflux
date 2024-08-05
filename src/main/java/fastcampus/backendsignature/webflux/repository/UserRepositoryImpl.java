package fastcampus.backendsignature.webflux.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final ConcurrentHashMap<Long, User> userMap = new ConcurrentHashMap<>();

    private final AtomicLong sequence = new AtomicLong(1L);

    @Override
    public Mono<User> save(User user) {
        var now = LocalDateTime.now();

        if (user.getId() == null) {
            user.setId(sequence.getAndAdd(1));
        }
        user.setUpdatedAt(now);
        userMap.put(user.getId(), user);
        return Mono.just(user);
    }

    @Override
    public Flux<User> findAll() {
        return Flux.fromIterable(userMap.values());
    }

    @Override
    public Mono<User> findById(Long id) {
        return Mono.justOrEmpty(userMap.getOrDefault(id, null));
    }

    @Override
    public Mono<Integer> deleteById(Long id) {
        var user = userMap.getOrDefault(id, null);
        if (user == null) {
            return Mono.just(0);
        }

        userMap.remove(id);
        return Mono.just(1);
    }
}
