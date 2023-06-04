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
        this.postService.save(Post.builder()
                        .authorId(account.getId())
                        .title(request.title)
                        .description(request.description)
                        .tags(request.tags)
                        .imageUrl(request.imageUrl)
                        .build())
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
        return Mono.empty();
    }

    public record CreatePostRequest(String title, String description, Tag[] tags, String imageUrl) {

    }
}
