package com.behl.flare.mappers;

import com.behl.flare.dto.EventCardRequest;
import com.behl.flare.dto.EventCardResponse;
import com.behl.flare.entity.EventCard;
import com.behl.flare.entity.User;
import com.behl.flare.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring"
)
public abstract class EventCardMapper {

    @Autowired
    protected UserJpaRepository userRepository;


//    @Mapping(target = "creator", source = "")
    @Mapping(target = "creator", source = "creatorId")
    @Mapping(target = "id", ignore = true)
    public abstract EventCard toEntity(EventCardRequest request);


    @Mapping(target = "creatorId", source = "creator.id")
    public abstract EventCardResponse toResponse(EventCard eventCard);


    @Mapping(target = "creator", source = "creatorId")
    @Mapping(target = "id", ignore = true)
    public abstract void updateEntityFromRequest(EventCardRequest request, @MappingTarget EventCard entity);


//    @Transactional
    public User creatorIdToUser(@NonNull String creatorFirebaseId) {
        User user = userRepository.findByFirebaseId(creatorFirebaseId).orElseThrow();
        return user;
    }


}
