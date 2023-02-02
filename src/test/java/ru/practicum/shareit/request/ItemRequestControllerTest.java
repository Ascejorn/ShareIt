package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDto requestInfoDto;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String HEADER = "X-Sharer-User-Id";

    @Test
    @DisplayName("Create request")
    void createRequest() throws Exception {

        itemRequestDto = ItemRequestDto.builder()
                .id(null)
                .description("TestDescription")
                .created(null)
                .items(null)
                .build();

        requestInfoDto = ItemRequestDto.builder()
                .id(null)
                .description("TestDescription")
                .created(null)
                .items(null)
                .build();

        Mockito.when(itemRequestService.create(Mockito.any(ItemRequestDto.class), Mockito.anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(HEADER, 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(requestInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.description",
                        Matchers.is(requestInfoDto.getDescription()),
                        String.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .create(Mockito.any(ItemRequestDto.class), Mockito.any(Long.class));
    }

    @Test
    void getRequestsByRequesterId() throws Exception {

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("TestDescription")
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        Mockito.when(itemRequestService.getItemRequestsByRequesterId(Mockito.anyLong()))
                .thenReturn(Collections.singletonList(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .header(HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description",
                        Matchers.is(itemRequestDto.getDescription()),
                        String.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .getItemRequestsByRequesterId(Mockito.any(Long.class));
    }
}