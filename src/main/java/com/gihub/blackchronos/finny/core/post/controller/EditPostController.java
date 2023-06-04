package com.gihub.blackchronos.finny.core.post.controller;

import com.gihub.blackchronos.finny.core.account.model.Account;
import com.gihub.blackchronos.finny.core.post.PostService;
import com.gihub.blackchronos.finny.core.post.exception.PostEditException;
import com.gihub.blackchronos.finny.core.post.model.Tag;
import com.gihub.blackchronos.finny.core.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class EditPostController {
    private final PostRepository postRepository;
    private final PostService postService;

    @MessageMapping("editPost")
    public Mono<Void> editPost(@AuthenticationPrincipal Account account, @RequestBody EditPostRequest request) {
        this.postRepository.findById(request.postId)
                .filter(post -> post.authorId == account.getId())
                .switchIfEmpty(new PostEditException("Post not found in ur posts").toMono())
                .flatMap(post -> {
                    post.title = request.title;
                    post.description = request.description;
                    post.tags = request.tags;
                    post.imageUrl = request.imageUrl;
                    return this.postService.save(post);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
        return Mono.empty();
    }

    public record EditPostRequest(String postId, String title, String description, Tag[] tags, String imageUrl) {

    }
}
