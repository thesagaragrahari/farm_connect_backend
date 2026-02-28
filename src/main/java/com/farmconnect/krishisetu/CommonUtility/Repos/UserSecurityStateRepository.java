package com.farmconnect.krishisetu.CommonUtility.Repos;


import com.farmconnect.krishisetu.CommonUtility.Entities.UserSecurityState;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSecurityStateRepository
        extends JpaRepository<UserSecurityState, Long> {

    // Find security state by userId (PK)
    Optional<UserSecurityState> findById(Long userId); // already provided by JpaRepository

    // Optional future extensions ↓↓↓

    Optional<UserSecurityState> findByUserId(Long userId); // not needed since userId is @Id

    List<UserSecurityState> findByAccountLockedTrue();

    List<UserSecurityState> findByEmailVerifiedFalse();
}
