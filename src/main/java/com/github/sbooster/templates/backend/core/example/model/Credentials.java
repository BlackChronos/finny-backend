package com.github.sbooster.templates.backend.core.example.model;

import com.github.sbooster.templates.backend.rsocket.security.model.StoredUserDetails;
import com.github.sbooster.templates.backend.util.JwtUtils;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.Pattern;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@Document("credentials")
public class Credentials implements StoredUserDetails {
    @MongoId(value = FieldType.INT64)
    @Builder.Default
    private Long id = (long) Math.abs(Objects.hashCode(ObjectId.get()));
    @Pattern(regexp = "sbooster.validator.email.regexp", message = "sbooster.validator.email.error")
    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;
    private boolean enabled;
    private boolean locked;

    public CredentialsToken generateToken() {
        CredentialsToken credentialsToken = new CredentialsToken();
        credentialsToken.setToken(JwtUtils.create(this, Duration.of(62, ChronoUnit.DAYS)));
        return credentialsToken;
    }
}
