package com.gihub.blackchronos.finny.core.post.controller;

import com.gihub.blackchronos.finny.core.post.model.Tag;
import com.gihub.blackchronos.finny.core.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SearchPostsController {
    private final PostRepository postRepository;

    @MessageMapping("searchPosts")
    public Flux<SearchPostsResponse> searchPosts(@RequestBody SearchPostsRequest request) {
        return this.postRepository.findAll()
                .filter(post -> {
                    Long authorId = request.authorId;
                    if (authorId != null) {
                        return Objects.equals(authorId, post.authorId);
                    }
                    Tag[] tags = request.tags;
                    if (tags != null && tags.length > 0) {
                        return Set.of(post.tags).containsAll(Set.of(tags));
                    }
                    return true;
                })
                .map(post -> new SearchPostsResponse(post.id));
    }

    public record SearchPostsRequest(Long authorId, Tag[] tags) {

    }
    public record SearchPostsResponse(String postId){}

}
