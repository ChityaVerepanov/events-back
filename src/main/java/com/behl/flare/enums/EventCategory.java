package com.behl.flare.enums;

/**
 * Категория мероприятия
 * Кино, Театр, Выставка, Музей, Спорт
 */
public enum EventCategory {

    DEFAULT("Не выбрано"),
    CINEMA("Кино"),
    THEATRE("Театр"),
    EXHIBITION("Выставка"),
    MUSEUM("Музей"),
    SPORT("Спорт");

    private final String description;

    EventCategory(String description) {
        this.description = description;
    }

    public String translate() {
        return description;
    }

    public String getDescription() {
        return description;
    }
}
