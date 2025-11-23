package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String login);
}