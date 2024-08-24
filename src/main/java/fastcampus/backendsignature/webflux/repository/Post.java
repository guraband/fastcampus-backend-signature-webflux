package fastcampus.backendsignature.webflux.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    public static Post of(Long userId, String title, String contents) {
        return new Post(null, userId, title, contents, LocalDateTime.now(), null);
    }

    @Id
    private Long id;
    private Long userId;
    private String title;
    private String contents;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
