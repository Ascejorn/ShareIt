package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemAdvancedDtoTest {

    @Autowired
    private JacksonTester<ItemAdvancedDto> json;

    @Test
    void testItemAdvancedDto() throws Exception {
        ItemAdvancedDto dto = new ItemAdvancedDto(
                1L,
                "name",
                "description",
                true,
                new ItemAdvancedDto.Booking(1L, null,null,null,1L,2L),
                new ItemAdvancedDto.Booking(1L, null,null,null,1L,2L),
                List.of(ItemAdvancedDto.Comment.builder()
                        .id(1L)
                        .authorName("AuthorName")
                        .text("text")
                        .created(LocalDateTime.now().minusDays(1))
                        .build()),
                2L
        );
        JsonContent<ItemAdvancedDto> result = json.write(dto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments.size()").isEqualTo(1);
    }
}