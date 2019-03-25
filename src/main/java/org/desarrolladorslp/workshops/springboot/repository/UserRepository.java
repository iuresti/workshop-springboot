package org.desarrolladorslp.workshops.springboot.repository;

import org.desarrolladorslp.workshops.springboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByemail(String email);
}
