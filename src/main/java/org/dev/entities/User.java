package org.dev.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID userId;

    private String name;

    private String password;

    @OneToOne
    private RefreshToken token;

    @ManyToMany
    @JoinTable(
            name = "user_roles", // name of join table
            joinColumns = @JoinColumn(name = "user_id"), // FK to User
            inverseJoinColumns = @JoinColumn(name = "role_id") // FK to Role
    )
    private Set<Roles> roles = new HashSet<>();
}
