package com.svalero.musicApi.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @NotNull(message = "Email required")
    @Email(message = "Incorrect email format")
    private String email;

    @NotNull(message = "Password required")
    private String password;
}
