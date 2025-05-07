package com.behl.flare.mappers;

import com.behl.flare.entity.User;
import org.springframework.data.repository.Repository;

interface UserRepository extends Repository<User, Long> {
}
