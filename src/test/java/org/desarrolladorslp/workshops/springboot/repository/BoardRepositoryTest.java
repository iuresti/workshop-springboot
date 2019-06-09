package org.desarrolladorslp.workshops.springboot.repository;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BoardRepository boardRepository;

    @Before
    public void setup() {
        entityManager.clear();
    }

    @Test
    public void givenUserAndBoardRelationshipInStorage_whenFindBoardByValidIdAndUserId_thenRetrieveBoard() {
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

        // when
        Optional<Board> repoBoard01 = boardRepository.findByIdAndUserId(board01.getId(), user01.getId());

        // then
        assertTrue(repoBoard01.isPresent());
        assertTrue(board01.getId().equals(repoBoard01.get().getId()));
        assertTrue(board01.getUser().getId().equals(repoBoard01.get().getUser().getId()));

    }

    @Test
    public void givenUserAndBoardRelationshipInStorage_whenFindBoardByValidIdAndInvalidUserId_thenRetrieveNothing() {
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

        // when
        Optional<Board> repoBoard00 = boardRepository.findById(board01.getId());
        Optional<Board> repoBoard01 = boardRepository.findByIdAndUserId(board01.getId(), 15L);

        // then
        assertTrue(repoBoard00.isPresent());
        assertFalse(repoBoard01.isPresent());

    }

    @Test
    public void givenUserAndBoardRelationshipInStorage_whenFindBoardByInvalidIdAndValidUserId_thenRetrieveNothing() {
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

        // when
        Optional<Board> repoBoard00 = boardRepository.findById(15L);
        Optional<Board> repoBoard01 = boardRepository.findByIdAndUserId(15L, user01.getId());

        // then
        assertFalse(repoBoard00.isPresent());
        assertFalse(repoBoard01.isPresent());

    }

    @Test
    public void givenUserAndBoardRelationshipInStorage_whenCheckExistByBoardAndId_thenIsTrue() {
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

        // when
        boolean doesExist = boardRepository.existsByIdAndUserId(board01.getId(), user01.getId());

        // then
        assertTrue(doesExist);
    }

    @Test
    public void givenNoUserAndBoardRelationshipInStorage_whenCheckExistByBoardAndId_thenIsFalse() {
        // when
        boolean doesExist = boardRepository.existsByIdAndUserId(1L, 1L);

        // then
        assertFalse(doesExist);
    }

}
