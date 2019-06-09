package org.desarrolladorslp.workshops.springboot.repository;

import java.util.Optional;

import org.desarrolladorslp.workshops.springboot.models.Role;
import org.desarrolladorslp.workshops.springboot.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
