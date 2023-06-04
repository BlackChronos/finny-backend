package com.gihub.blackchronos.finny.core.post.repository;

import com.gihub.blackchronos.finny.core.post.model.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, UUID> {
    Flux<Post> findAllByAuthorId(long authorId);
}
