package com.rakesh.peer_interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.peer_interview.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
