package org.dev.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String name;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private RefreshToken token;

    @ManyToMany
    @JoinTable(
            name = "user_roles", // name of join table
            joinColumns = @JoinColumn(name = "user_id"), // FK to User
            inverseJoinColumns = @JoinColumn(name = "role_id") // FK to Role
    )
    private Set<Roles> roles = new HashSet<>();
}
