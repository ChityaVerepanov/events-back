package com.behl.flare.mappers;

import com.behl.flare.dto.eventcard.EventCardRequest;
import com.behl.flare.dto.eventcard.EventCardResponse;
import com.behl.flare.entity.EventCard;
import com.behl.flare.entity.User;
import com.behl.flare.repository.UserJpaRepository;
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
    public User creatorIdToUser(@NonNull Long creatorId) {
//        User user = userRepository.findByFirebaseId(creatorFirebaseId).orElseThrow();
        User user = userRepository.findById(creatorId).orElseThrow();
        return user;
    }


}
