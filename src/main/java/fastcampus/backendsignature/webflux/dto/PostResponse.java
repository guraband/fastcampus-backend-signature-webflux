package fastcampus.backendsignature.webflux.dto;

import fastcampus.backendsignature.webflux.repository.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponse {
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
    }

    private Long id;
    private String title;
    private String contents;
}
