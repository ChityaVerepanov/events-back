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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Каточка мероприятия
 */
@Getter
@Setter
@Entity
@Table(name = "event_card")
public class EventCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текст в карточке
     */
    @Column(name = "message", nullable = false)
    private String message;

    /**
     * Имя файла картинки, привязанного к карточке
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * Создатель карточки
     */
//    @Column(name = "creator", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

}
