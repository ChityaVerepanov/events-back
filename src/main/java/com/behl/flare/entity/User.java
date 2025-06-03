package com.behl.flare.entity;

import com.behl.flare.enums.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "users", indexes = @Index(columnList = "firebaseId"))
public class User {

    @EqualsAndHashCode.Include
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
    private Roles role = Roles.ROLE_USER;


    //    @OneToMany(fetch = FetchType.EAGER)
//    private List<EventCard> favoriteEvents = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_favorite_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_card_id")
    )
    private Set<EventCard> favoriteEvents = new HashSet<>();

    //    @OneToMany(fetch = FetchType.EAGER)
//    private List<EventCard> plannedEvents = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_planned_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_card_id")
    )
    private Set<EventCard> plannedEvents = new HashSet<>();

    /**
     * Имя файла картинки, привязанного к карточке
     */
    @Column(name = "file_name")
    private String fileName;


    public boolean isCreatorOf(EventCard eventCard) {
        return eventCard.getCreator().getId().equals(id);
    }

    public boolean isAdmin() {
        return role == Roles.ROLE_ADMIN;
    }
}
