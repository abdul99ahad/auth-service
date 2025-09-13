package org.dev.repositories;

import org.dev.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
}
