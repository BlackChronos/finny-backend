package com.gihub.blackchronos.finny.core.post.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Document("posts")
public class Post {
    @Id
    public String id;
    public long authorId;

    public String title;
    public String description;
    public Tag[] tags;
    public String imageUrl;
    @Builder.Default
    public Instant instant = Instant.now();
}
