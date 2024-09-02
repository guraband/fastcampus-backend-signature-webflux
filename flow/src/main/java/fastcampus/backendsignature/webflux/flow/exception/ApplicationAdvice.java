package fastcampus.backendsignature.webflux.flow.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ApplicationAdvice {
    @ExceptionHandler(ApplicationException.class)
    Mono<ResponseEntity<ServerExceptionReason>> applicationExceptionHandler(ApplicationException ae) {
        return Mono.just(
                ResponseEntity
                        .status(ae.getHttpStatus())
                        .body(new ServerExceptionReason(ae.getCode(), ae.getReason()))
        );
    }

    public record ServerExceptionReason(String code, String reason) {

    }
}
