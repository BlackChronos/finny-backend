package com.gihub.blackchronos.finny.core.post.controller;

import com.gihub.blackchronos.finny.core.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SubscribeNewPostsController {
    private final PostService postService;

    @MessageMapping("subscribeNewPosts")
    public Flux<UUID> subscribeNewPosts() {
        return this.postService.getFlux().map(post -> post.id);
    }
}
