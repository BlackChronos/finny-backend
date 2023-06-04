package com.gihub.blackchronos.finny.core.post.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.UUID;

@Document("posts")
public class Post {
    @MongoId
    public UUID id;
    public long authorId;
    public String message;
    public Tag[] tags;
    public String imageUrl;
    public Instant instant = Instant.now();
}
