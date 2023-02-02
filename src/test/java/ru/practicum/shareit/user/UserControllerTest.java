package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;
    private UserDto userDto;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Create user")
    void create() throws Exception {

        userDto = UserDto.builder()
                .id(null)
                .name("User")
                .email("user@mail.com")
                .build();

        Mockito.when(userService.create(Mockito.any(UserDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name", Matchers.is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userDto.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1))
                .create(Mockito.any(UserDto.class));
    }

    @Test
    @DisplayName("Update user")
    void update() throws Exception {

        userDto = UserDto.builder()
                .id(null)
                .name("User")
                .email("user@mail.com")
                .build();

        Mockito.when(userService.update(Mockito.any(UserDto.class), Mockito.anyLong()))
                .thenReturn(userDto);

        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userDto.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1))
                .update(Mockito.any(UserDto.class), Mockito.anyLong());
    }


    @Test
    @DisplayName("Get user by id")
    void getById() throws Exception {

        userDto = UserDto.builder()
                .id(1L)
                .name("User")
                .email("user@mail.com")
                .build();

        Mockito.when(userService.getById(Mockito.anyLong()))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userDto.getEmail()), String.class));
    }

    @Test
    @DisplayName("Get all users")
    void getAll() throws Exception {

        userDto = UserDto.builder()
                .id(null)
                .name("User")
                .email("user@mail.com")
                .build();

        Mockito.when(userService.getAll())
                .thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));

        Mockito.verify(userService, Mockito.times(1))
                .getAll();
    }

    @Test
    @DisplayName("Delete user by id")
    void delete() {
        userService.delete(1L);
        Mockito.verify(userService, Mockito.times(1)).delete(1L);
    }
}