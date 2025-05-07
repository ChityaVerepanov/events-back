package com.behl.flare.dto;

import com.behl.flare.enums.Roles;
import java.util.ArrayList;
import java.util.List;
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
    private List<Long> eventCards = new ArrayList<>();

}
