package com.behl.flare.entity;

import com.behl.flare.enums.Roles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users", indexes = @Index(columnList = "firebaseId"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firebase_id", nullable = false, unique = true)
    private String firebaseId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
//    private Roles role = Roles.USER;
    private Roles role = Roles.ROLE_USER;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    private List<EventCard> eventCards = new ArrayList<>();

}
