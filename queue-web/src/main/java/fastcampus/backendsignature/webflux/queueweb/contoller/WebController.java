package fastcampus.backendsignature.webflux.queueweb.contoller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Controller
@RequestMapping("")
public class WebController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping({"", "/"})
    public String index(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "user_id") Long userId,
            HttpServletRequest request
    ) {
        var cookies = request.getCookies();
        var cookieName = "user-queue-%s-token".formatted(queue);

        var token = "";
        if (cookies != null) {
            token = Arrays.stream(cookies)
                    .filter(i -> i.getName().equals(cookieName))
                    .findFirst()
                    .orElse(new Cookie(cookieName, ""))
                    .getValue();
        }
        var uri =
                UriComponentsBuilder
                        .fromHttpUrl("http://127.0.0.1:9010")
                        .path("/api/v1/queue/allowed")
                        .queryParam("queue", queue)
                        .queryParam("user_id", userId)
                        .queryParam("token", token)
                        .encode()
                        .build()
                        .toUri();

        ResponseEntity<AllowedUserResponse> response = restTemplate.getForEntity(uri, AllowedUserResponse.class);
        if (response.getBody() == null || !response.getBody().isAllowed()) {
            return "redirect:http://127.0.0.1:9010/waiting-room?queue=%s&user_id=%d&redirect_url=%s"
                    .formatted(queue, userId, "http://127.0.0.1:9000?user_id=%d".formatted(userId));
        }
        return "index";
    }

    public record AllowedUserResponse(Boolean isAllowed) {
    }
}
