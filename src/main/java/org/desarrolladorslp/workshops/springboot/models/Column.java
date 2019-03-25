package org.desarrolladorslp.workshops.springboot.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "columns",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "name"})
        })
public class Column {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @javax.persistence.Column(length = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
