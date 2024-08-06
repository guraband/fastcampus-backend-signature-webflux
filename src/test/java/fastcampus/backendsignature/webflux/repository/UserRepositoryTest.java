package fastcampus.backendsignature.webflux.repository;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTest {
    private final UserRepository userRepository = new UserRepositoryImpl();

    @Test
    void save() {
        var user = User.newUser("페페", "pepe@gmail.com");

        StepVerifier.create(userRepository.save(user))
                .assertNext(u -> {
                    assertEquals(u.getName(), "페페");
                    assertEquals(u.getEmail(), "pepe@gmail.com");
                })
                .verifyComplete();
    }

    @Test
    void findAll() {
        var user1 = User.newUser("페페", "pepe@gmail.com");
        var user2 = User.newUser("페페2", "pepe2@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        StepVerifier.create(userRepository.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {
        var user1 = new User(1L, "페페", "pepe@gmail.com", null, null);
        var user2 = new User(2L, "페페2", "pepe2@gmail.com", null, null);
        userRepository.save(user1);
        userRepository.save(user2);

        StepVerifier.create(userRepository.findById(1L))
                .assertNext(user -> {
                    assertEquals(user.getId(), user1.getId());
                    assertEquals(user.getName(), user1.getName());
                })
                .verifyComplete();
    }

    @Test
    void deleteById() {
        var user1 = new User(1L, "페페", "pepe@gmail.com", null, null);
        var user2 = new User(2L, "페페2", "pepe2@gmail.com", null, null);
        userRepository.save(user1);
        userRepository.save(user2);

        StepVerifier.create(userRepository.deleteById(1L))
                .assertNext(i -> {
                    assertEquals(i, 1);
                })
                .verifyComplete();
    }
}