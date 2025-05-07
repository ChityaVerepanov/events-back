package com.behl.flare.repository;

import com.behl.flare.entity.User;
import com.behl.flare.enums.Roles;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByFirebaseId(String firebaseId);

    List<User> findByEmail(String email);

    List<User> findByEmailAndRoleAndFirebaseId(String email, Roles role, String firebaseId);
}
