package org.desarrolladorslp.workshops.springboot.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Card;
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
public class CardRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardRepository cardRepository;

    @Before
    public void setup() {
        entityManager.clear();
    }

    @Test
    public void givenUserAndColumnRelationship_whenFindCardsByColumn_thenRetrieveColumnCards() {
        // given
        User user01 = new User();
        user01.setUsername("user01"); user01.setName("user01");
        user01.setPassword("hash3dp4ssw0rd");

        user01 = entityManager.persist(user01);

        Board board01 = new Board();
        board01.setUser(user01);
        board01.setName("Board01");

        board01 = entityManager.persist(board01);

        Column column01 = new Column();
        column01.setName("Column01");
        column01.setBoard(board01);

        column01 = entityManager.persist(column01);

        Card card01 = new Card();
        card01.setDescription("Desc1"); card01.setColumn(column01);

        Card card02 = new Card();
        card02.setDescription("Desc2"); card02.setColumn(column01);

        card01 = entityManager.persist(card01);
        card02 = entityManager.persist(card02);

        // when
        List<Card> cards = cardRepository.findByColumn(column01.getId());

        // then
        assertNotNull(cards);
        assertTrue(cards.size() == 2);
        assertEquals(cards, Arrays.asList(card01, card02));
    }

    @Test
    public void givenNoUserAndColumnRelationship_whenFindCardsByColumn_thenRetrieveNoneColumnCards() {
        // given
        User user01 = new User();
        user01.setUsername("user01"); user01.setName("user01");
        user01.setPassword("hash3dp4ssw0rd");

        user01 = entityManager.persist(user01);

        Board board01 = new Board();
        board01.setUser(user01);
        board01.setName("Board01");

        board01 = entityManager.persist(board01);

        Column column01 = new Column();
        column01.setName("Column01");
        column01.setBoard(board01);

        Column column02 = new Column();
        column02.setName("Column02");
        column02.setBoard(board01);

        column01 = entityManager.persist(column01);
        column02 = entityManager.persist(column02);

        Card card01 = new Card();
        card01.setDescription("Desc1"); card01.setColumn(column01);

        Card card02 = new Card();
        card02.setDescription("Desc2"); card02.setColumn(column01);

        entityManager.persist(card01);
        entityManager.persist(card02);

        // when
        List<Card> cards = cardRepository.findByColumn(column02.getId());

        // then
        assertEquals(cards, Collections.emptyList());
    }

}
