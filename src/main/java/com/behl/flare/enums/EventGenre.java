package com.behl.flare.enums;

/**
 * Жанр мероприятия
 * Драма, Комедия, Романтика, Боевик, Триллер, Ужасы, Историческое
 */
public enum EventGenre {

    DEFAULT("Не выбрано"),
    DRAMA("Драма"),
    COMEDY("Комедия"),
    ROMANCE("Романтика"),
    ACTION("БОЕВИК"),
    THRILLER("Триллер"),
    HORROR("Ужасы"),
    HISTORICAL("Историческое");

    private final String description;

    EventGenre(String description) {
        this.description = description;
    }

    public String translate() {
        return description;
    }

    public String getDescription() {
        return description;
    }
}
