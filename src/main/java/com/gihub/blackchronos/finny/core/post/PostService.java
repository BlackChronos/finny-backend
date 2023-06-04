package com.gihub.blackchronos.finny.core.post;

import com.gihub.blackchronos.finny.core.post.model.Post;
import com.gihub.blackchronos.finny.core.post.repository.PostRepository;
import com.gihub.blackchronos.finny.emitter.besteffort.SharedMulticastBestEffortEmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService extends SharedMulticastBestEffortEmitter<Post> {
    private final PostRepository postRepository;

    public Mono<Post> save(Post post) {
        return this.postRepository.save(post).map(this::emit);
    }
}
