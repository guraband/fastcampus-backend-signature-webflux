package fastcampus.backendsignature.webflux.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class User {
    public static User newUser(String name, String email) {
        return new User(null, name, email, LocalDateTime.now(), null);
    }

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
