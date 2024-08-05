package fastcampus.backendsignature.webflux.service;

import fastcampus.backendsignature.webflux.repository.User;
import fastcampus.backendsignature.webflux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<User> create(String name, String email) {
        return userRepository.save(User.newUser(name, email));
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<User> update(Long id, String name, String email) {
        return userRepository.findById(id)
                .flatMap(user -> {
                    user.setName(name);
                    user.setEmail(email);
                    return userRepository.save(user);
                });
    }

    public Mono<Integer> delete(Long id) {
        return userRepository.deleteById(id);
    }
}
