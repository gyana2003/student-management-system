package com.student.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLES")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole name;

    // Default Constructor
    public Role() {
    }

    // All-args Constructor
    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    public enum ERole {
        ROLE_STUDENT,
        ROLE_TEACHER,
        ROLE_ADMIN
    }
}
