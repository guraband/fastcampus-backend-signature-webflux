package fastcampus.backendsignature.webflux.flow.dto;

public record RegisterUserResponse(
        Long rank
) {
    public RegisterUserResponse(Long rank) {
        this.rank = rank + 1;
    }
}
