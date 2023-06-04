package com.gihub.blackchronos.finny.core.post.model;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.UUID;

@Builder
@Document("posts")
public class Post {
    @MongoId
    public UUID id;
    public long authorId;

    public String title;
    public String description;
    public Tag[] tags;
    public String imageUrl;
    public Instant instant = Instant.now();
}
