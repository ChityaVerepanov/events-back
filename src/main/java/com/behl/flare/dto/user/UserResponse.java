package com.behl.flare.dto.user;

import com.behl.flare.enums.Roles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String firebaseId;
    private String email;
    private String phoneNumber;
    private String displayName;
    private Roles role;
//    private List<Long> favoriteEvents = new ArrayList<>();
//    private List<Long> plannedEvents = new ArrayList<>();
    private Set<Long> favoriteEvents = new HashSet<>();
    private Set<Long> plannedEvents = new HashSet<>();

}
