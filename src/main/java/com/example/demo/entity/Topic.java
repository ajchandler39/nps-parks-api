package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    private String id;

    private String name;

    @ManyToMany(mappedBy = "topics")
    private Set<Park> parks = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Park> getParks() {
        return parks;
    }

    public void setParks(Set<Park> parks) {
        this.parks = parks;
    }
}