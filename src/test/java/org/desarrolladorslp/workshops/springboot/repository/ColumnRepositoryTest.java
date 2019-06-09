package org.desarrolladorslp.workshops.springboot.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;
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
public class ColumnRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ColumnRepository columnRepository;

    @Before
    public void setup() {
        entityManager.clear();
    }

    @Test
    public void it_should_return_true_when_given_a_relationship_between_user_and_column() {
        // given
        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .password("notyetencodedpassword")
                .enabled(true)
                .build();

        user01 = entityManager.persist(user01);

        Board board01 = new Board();
        board01.setName("Sample");
        board01.setUser(user01);

        board01 = entityManager.persist(board01);

        Column column01 = new Column();
        column01.setName("Column");
        column01.setBoard(board01);

        column01 = entityManager.persist(column01);

        // when
        boolean doesExist = columnRepository.existsByIdAndUserId(column01.getId(), user01.getId());

        // then
        assertTrue(doesExist);
    }

    @Test
    public void it_should_return_false_when_given_no_relationship_between_user_and_column() {
        // given
        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .password("notyetencodedpassword")
                .enabled(true)
                .build();

        User user02 = new User().builder()
                .email("user02@example.com")
                .username("user02")
                .name("user02")
                .password("notyetencodedpassword")
                .enabled(true)
                .build();

        user01 = entityManager.persist(user01);
        user02 = entityManager.persist(user02);

        Board board01 = new Board();
        board01.setName("Sample");
        board01.setUser(user01);

        board01 = entityManager.persist(board01);

        Column column01 = new Column();
        column01.setName("Column");
        column01.setBoard(board01);

        column01 = entityManager.persist(column01);

        // when
        boolean doesExist = columnRepository.existsByIdAndUserId(column01.getId(), user02.getId());

        // then
        assertFalse(doesExist);
    }
}
