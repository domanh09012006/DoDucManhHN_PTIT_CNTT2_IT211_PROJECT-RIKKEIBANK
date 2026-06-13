package org.example.rikkeibank.repository;

import org.example.rikkeibank.dto.response.UserResponse;
import org.example.rikkeibank.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("""
            select new org.example.rikkeibank.dto.response.UserResponse(
                u.id, u.username, u.fullName, u.email, u.phone, u.role, u.isKyc, u.enabled
            )
            from User u
            where u.enabled = true
            """)
    Page<UserResponse> findAllActiveUsers(Pageable pageable);
}
