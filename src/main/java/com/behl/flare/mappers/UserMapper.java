package com.behl.flare.mappers;

import com.behl.flare.dto.UserResponse;
import com.behl.flare.entity.EventCard;
import com.behl.flare.entity.User;
import com.google.firebase.auth.FirebaseToken;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(source = "eventCards", target = "eventCards"/*, qualifiedByName = "mapEventCardIds"*/)
    public abstract UserResponse toResponse(User entity);


//    public abstract User toEntity(String firebaseId, FirebaseToken firebaseToken);


//    @Mapping(source = "firebaseToken.phoneNumber", target = "phoneNumber")
    @Mapping(target = "phoneNumber", expression = "java(getFirebaseTokenClaim(firebaseToken, \"phoneNumber\"))")
    @Mapping(source = "firebaseId", target = "firebaseId")
    @Mapping(source = "firebaseToken.email", target = "email")
//    @Mapping(source = "firebaseToken.displayName", target = "displayName")
    @Mapping(target = "displayName", expression = "java(getFirebaseTokenClaim(firebaseToken, \"displayName\"))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventCards", ignore = true)
    @Mapping(target = "role", ignore = true)
    public abstract User toEntity(String firebaseId, FirebaseToken firebaseToken);



//    @Named("mapEventCardIds")
    List<Long> mapEventCardIds(List<EventCard> eventCards) {
        if (eventCards == null) {
            return Collections.emptyList();
        }
        return eventCards.stream().map(EventCard::getId).toList();
    }

    protected String getFirebaseTokenClaim(FirebaseToken firebaseToken, String key) {
        return Optional.ofNullable(firebaseToken)
                .map(FirebaseToken::getClaims)
                .map(map -> map.get(key))
                .map(Object::toString)
                .orElse("");
    }


}
