package org.desarrolladorslp.workshops.springboot.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.desarrolladorslp.workshops.springboot.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Before
    public void setup() {
        entityManager.clear();
    }

    @Test
    public void givenUserInStorage_whenFindByValidUsername_thenRetrieveUser() {
        // given
        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .password("notyetencodedpassword")
                .enabled(true)
                .build();
        entityManager.persist(user01);

        // when
        Optional<User> repoUser01 = userRepository.findByUsername("user01");

        // then
        assertTrue(repoUser01.isPresent());
        assertTrue(user01.getId().equals(repoUser01.get().getId()));
        assertTrue(user01.getUsername().equals(repoUser01.get().getUsername()));

    }

    @Test
    public void givenUserInStorage_whenFindByInvalidUsername_thenRetrieveEmpty() {
        // given
        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .password("notyetencodedpassword")
                .enabled(true)
                .build();
        entityManager.persist(user01);

        // when
        Optional<User> repoUser02 = userRepository.findByUsername("user02");

        // then
        assertFalse(repoUser02.isPresent());

    }
}
