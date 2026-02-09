package io.github.platovd.userserver.user;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class User {
    @Id
    private String id;

    @Field(name = "username")
    private String username;

    @Field(name = "email")
    private String email;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "last_name")
    private String lastName;

    @Field(name = "auth_provider")
    private AuthProvider provider;

    @Field(name = "provider_user_id")
    private String providerUserId;

    @Field(name = "user_status")
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

    @CreatedDate
    @Field(name = "creation_date")
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Field(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
