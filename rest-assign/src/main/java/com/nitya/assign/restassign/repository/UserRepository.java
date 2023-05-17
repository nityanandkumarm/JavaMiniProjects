package com.nitya.assign.restassign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nitya.assign.restassign.entity.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

}
