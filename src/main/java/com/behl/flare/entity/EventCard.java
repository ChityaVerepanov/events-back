package com.behl.flare.entity;

import com.behl.flare.enums.EventCategory;
import com.behl.flare.enums.EventGenre;
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
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Каточка мероприятия
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "event_card")
public class EventCard {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текст в карточке
     */
/*
    @Column(name = "message", nullable = false)
    private String message;
*/

    /**
     * Имя файла картинки, привязанного к карточке
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * Имя мероприятия
     */
    @Column(name = "event_name")
    private String eventName;

    /**
     * Описание мероприятия
     */
    @Column(name = "event_description")
    private String eventDescription;

    /**
     * Дата начала
     */
    @Column(name = "date_start")
    private LocalDate dateStart;

    /**
     * Дата конца
     */
    @Column(name = "date_end")
    private LocalDate dateEnd;

    /**
     * Местоположение
     */
    @Column(name = "place")
    private String place;

    /**
     * Название организатора
     */
    @Column(name = "organizer_name")
    private String organizerName;

    /**
     * Сайт компании
     */
    @Column(name = "organizer_site")
    private String organizerSite;

    /**
     * Категория мероприятия
     */
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private EventCategory category = EventCategory.DEFAULT;

    /**
     * Жанр мероприятия
     */
    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    private EventGenre genre = EventGenre.DEFAULT;

    /**
     * Жанр мероприятия
     */
    @Column(name = "cost")
    private Integer cost;



    /**
     * Создатель карточки
     */
//    @Column(name = "creator", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;



}
