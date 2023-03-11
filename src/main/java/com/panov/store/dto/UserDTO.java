package com.panov.store.dto;

import com.panov.store.model.Address;
import com.panov.store.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;

    private User.PersonalInfo personalInfo;

    private Address address;

    public static UserDTO of(User u) {
        if (u == null)
            return null;
        return new UserDTO(
            u.getUserId(),
            u.getPersonalInfo(),
            u.getAddress()
        );
    }

    public User toModel() {
        var u = new User();
        u.setUserId(userId);
        u.setPersonalInfo(personalInfo);
        u.setAddress(address);
        u.setOrders(new ArrayList<>());
        return u;
    }
}
