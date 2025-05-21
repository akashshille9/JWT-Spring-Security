package com.example.SpringSecDemo.repository;

import com.example.SpringSecDemo.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthEntityRepository extends JpaRepository<UserAuthEntity,Long> {

     Optional<UserAuthEntity> findByUsername(String username);
}
