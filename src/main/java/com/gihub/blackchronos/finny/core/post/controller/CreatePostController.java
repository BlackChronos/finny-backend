package com.gihub.blackchronos.finny.core.post.controller;

import com.gihub.blackchronos.finny.core.account.model.Account;
import com.gihub.blackchronos.finny.core.post.PostService;
import com.gihub.blackchronos.finny.core.post.model.Post;
import com.gihub.blackchronos.finny.core.post.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Controller
@RequiredArgsConstructor
public class CreatePostController {
    private final PostService postService;

    @MessageMapping("createPost")
    @PreAuthorize("isAuthenticated()")
    public Mono<Void> createPost(@AuthenticationPrincipal Account account, @RequestBody CreatePostRequest request) {
        Post post = new Post();
        post.authorId = account.getId();
        post.message = request.message;
        post.tags = request.tags;
        post.imageUrl = request.imageUrl;
        this.postService.save(post)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
        return Mono.empty();
    }

    public record CreatePostRequest(String message, Tag[] tags, String imageUrl) {

    }
}
