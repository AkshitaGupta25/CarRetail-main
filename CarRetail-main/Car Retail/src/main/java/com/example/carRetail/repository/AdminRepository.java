package com.example.carRetail.repository;

import com.example.carRetail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    List<User> findAllByDeleteStatus(boolean deleteStatus);

    List<User> findAllByDeleteStatusAndRoleId(boolean deleteStatus, Long roleId);

    boolean existsByEmail(String email);
}