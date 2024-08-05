package fastcampus.backendsignature.webflux.dto;

import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String name;
    private String email;
}
