package com.example.healthsync.dto;

import com.example.healthsync.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String message;
    private String email;
    private Role role;

}
