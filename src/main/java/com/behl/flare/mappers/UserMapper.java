package com.behl.flare.mappers;

import com.behl.flare.dto.UserResponse;
import com.behl.flare.entity.EventCard;
import com.behl.flare.entity.User;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(source = "eventCards", target = "eventCards"/*, qualifiedByName = "mapEventCardIds"*/)
    public abstract UserResponse toResponse(User entity);


//    @Named("mapEventCardIds")
    List<Long> mapEventCardIds(List<EventCard> eventCards) {
        if (eventCards == null) {
            return Collections.emptyList();
        }
        return eventCards.stream().map(EventCard::getId).toList();
    }
}
