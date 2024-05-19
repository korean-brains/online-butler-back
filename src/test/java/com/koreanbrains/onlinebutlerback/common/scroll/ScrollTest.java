package com.koreanbrains.onlinebutlerback.common.scroll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ScrollTest {

    @Test
    @DisplayName("스크롤 본문 데이터를 변경한다")
    void map() {
        // given
        Scroll<String> scroll = new Scroll<>(List.of("item 1", "item 2", "item 3"), null, null);

        // when
        Scroll<String> convertScroll = scroll.map((c) -> c + " modify");

        // then
        assertThat(convertScroll.getContent().get(0)).isEqualTo("item 1 modify");
        assertThat(convertScroll.getContent().get(1)).isEqualTo("item 2 modify");
        assertThat(convertScroll.getContent().get(2)).isEqualTo("item 3 modify");
    }

}