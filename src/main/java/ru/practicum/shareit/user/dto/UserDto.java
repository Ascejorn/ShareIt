package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {

    Long id;

    @NotNull(message = "Name/Login is required.", groups = {Create.class})
    @NotBlank(message = "Name/Login is required.", groups = {Create.class})
    String name;

    @NotNull(message = "Email is required.", groups = {Create.class})
    @Email(message = "Email is invalid.", groups = {Create.class, Update.class})
    String email;
}
