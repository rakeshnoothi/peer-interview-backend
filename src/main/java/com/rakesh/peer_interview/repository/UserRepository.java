package com.rakesh.peer_interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.peer_interview.model.MyUser;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
	MyUser findByUsername(String username);
}
