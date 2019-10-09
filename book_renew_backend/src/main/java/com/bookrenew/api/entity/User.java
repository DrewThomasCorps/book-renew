package com.bookrenew.api.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private long id;
    private String username;
    private String password;
    private String name;
    private String email;
}
