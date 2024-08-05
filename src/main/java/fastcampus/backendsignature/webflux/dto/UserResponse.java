package fastcampus.backendsignature.webflux.dto;

import fastcampus.backendsignature.webflux.repository.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
