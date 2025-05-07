package com.behl.flare.repository;

import com.behl.flare.entity.EventCard;
import com.behl.flare.entity.User;
import com.behl.flare.enums.Roles;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCardJpaRepository extends JpaRepository<EventCard, Long>, JpaSpecificationExecutor<EventCard> {

}
