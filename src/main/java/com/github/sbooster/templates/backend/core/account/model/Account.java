package com.github.sbooster.templates.backend.core.account.model;

import com.github.sbooster.templates.backend.rsocket.security.model.StoredUserDetails;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@Document("credentials")
public class Account implements StoredUserDetails {
    @MongoId(value = FieldType.INT64)
    @Builder.Default
    private Long id = (long) Math.abs(Objects.hashCode(ObjectId.get()));
    @Pattern(regexp = "sbooster.validator.email.regexp", message = "sbooster.validator.email.error")
    private String username;
    @NotNull(message = "Account password can't be null")
    private String password;
    private Set<AccountType> authorities;
    private boolean enabled;

    /**
     * TODO У заблокированного пользователя должны быть указаны:
     *  Причина блокировка
     *  История блокировок
     *  Дата блокировки
     *  Дата окончания блокировки
     *  Информация о том кто заблокировал
     */
    private boolean locked;
    private Instant createdAt;
    private Instant lastOnlineAt;
}
