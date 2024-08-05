package fastcampus.backendsignature.webflux.dto;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private Long id;
    private String name;
    private String email;
}
