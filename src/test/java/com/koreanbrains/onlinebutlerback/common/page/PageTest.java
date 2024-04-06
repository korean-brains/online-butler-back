package com.koreanbrains.onlinebutlerback.common.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PageTest {

    @Test
    @DisplayName("생성자로 Page 인스턴스를 생성한다.")
    void newInstanceConstructor() {
        // given
        List<String> content = List.of("content 1", "content 2", "content 3", "content 4", "content 5");
        int number = 1;
        int size = 5;
        long totalElements = 10;

        // when
        Page<String> page = new Page<>(content, number, size, totalElements);

        // then
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isHasNext()).isTrue();
        assertThat(page.getNumber()).isEqualTo(1);
        assertThat(page.getContent().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("콘텐트 갯수가 0개라도 총 페이지 갯수는 1이어야 한다.")
    void totalPagesIsOne() {
        // given
        List<String> content = List.of();
        int number = 1;
        int size = 0;
        long totalElements = 0;

        // when
        Page<String> page = new Page<>(content, number, size, totalElements);

        // then
        assertThat(page.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("page 인스턴스의 콘텐트를 다른 데이터로 변경할 수 있다.")
    void convertContent() {
        // given
        List<String> content = List.of("content 1", "content 2", "content 3", "content 4", "content 5");
        int number = 1;
        int size = 0;
        long totalElements = 0;
        Page<String> page = new Page<>(content, number, size, totalElements);

        // when
        Page<String> result = page.map(c -> c + " changed");

        // then
        assertThat(result.getContent().get(0)).isEqualTo("content 1 changed");
        assertThat(result.getContent().get(1)).isEqualTo("content 2 changed");
        assertThat(result.getContent().get(2)).isEqualTo("content 3 changed");
        assertThat(result.getContent().get(3)).isEqualTo("content 4 changed");
        assertThat(result.getContent().get(4)).isEqualTo("content 5 changed");
    }

}