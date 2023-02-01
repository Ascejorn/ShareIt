package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L,
                "Grasscutter",
                List.of(ItemRequestDto.Item.builder()
                        .id(1L)
                        .name("name")
                        .description("description")
                        .available(true)
                        .requestId(2L)
                        .build()
                ),
                LocalDateTime.now());
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).extractingJsonPathValue("$.description").isEqualTo(itemRequestDto.getDescription());
    }
}