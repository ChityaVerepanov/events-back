package com.behl.flare.mappers;

import com.behl.flare.dto.user.UserResponse;
import com.behl.flare.entity.EventCard;
import com.behl.flare.entity.User;
import com.google.firebase.auth.FirebaseToken;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

//    @Mapping(source = "eventCards", target = "eventCards"/*, qualifiedByName = "mapEventCardIds"*/)
    public abstract UserResponse toResponse(User entity);

    @Mapping(target = "phoneNumber", expression = "java(getFirebaseTokenClaim(firebaseToken, \"phoneNumber\"))")
    @Mapping(source = "firebaseId", target = "firebaseId")
    @Mapping(source = "firebaseToken.email", target = "email")
    @Mapping(target = "displayName", expression = "java(getFirebaseTokenClaim(firebaseToken, \"displayName\"))")
    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "eventCards", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "plannedEvents", ignore = true)
    @Mapping(target = "favoriteEvents", ignore = true)
    public abstract User toEntity(String firebaseId, FirebaseToken firebaseToken);



//    @Named("mapEventCardIds")
/*
    List<Long> mapEventCardIds(List<EventCard> eventCards) {
        if (eventCards == null) {
            return Collections.emptyList();
        }
        return eventCards.stream().map(EventCard::getId).toList();
    }
*/

    Set<Long> mapEventCardIds(Set<EventCard> eventCards) {
        if (eventCards == null) {
            return Collections.emptySet();
        }
        return eventCards.stream().map(EventCard::getId).collect(Collectors.toSet());
    }

    protected String getFirebaseTokenClaim(FirebaseToken firebaseToken, String key) {
        return Optional.ofNullable(firebaseToken)
                .map(FirebaseToken::getClaims)
                .map(map -> map.get(key))
                .map(Object::toString)
                .orElse("");
    }


}
