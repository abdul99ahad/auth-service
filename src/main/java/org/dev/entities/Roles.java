package org.dev.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Roles {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private long roleId;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany
    private Set<User> users = new HashSet<>();
}
