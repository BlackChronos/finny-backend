package com.gihub.blackchronos.finny.core.post.controller;

import com.gihub.blackchronos.finny.core.post.PostService;
import com.gihub.blackchronos.finny.core.post.model.Post;
import com.gihub.blackchronos.finny.core.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SubscribePostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @MessageMapping("subscribePost")
    public Flux<Post> subscribePost(@RequestBody SubscribePostRequest request) {
        return this.postService.getFlux()
                .filter(post -> post.id.equals(request.postId))
                .mergeWith(this.postRepository.findById(request.postId));
    }

    public record SubscribePostRequest(UUID postId) {

    }
}
