package com.koreanbrains.onlinebutlerback.common.page;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PageRequest {
    @Builder.Default
    private int number = 1;
    @Builder.Default
    private int size = 10;
}
