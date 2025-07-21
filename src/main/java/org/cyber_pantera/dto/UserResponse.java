package org.cyber_pantera.dto;

import lombok.Data;

@Data
public class UserResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
