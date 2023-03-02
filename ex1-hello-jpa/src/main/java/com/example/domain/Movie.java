package com.example.domain;

import javax.persistence.Entity;

@Entity
public class Movie extends Item {
    private String director;
    private String actor;

    public void setActor(String actor) {
        this.actor = actor;
    }
    public void setDirector(String director) {
        this.director = director;
    }
}
