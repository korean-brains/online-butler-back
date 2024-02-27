package com.koreanbrains.onlinebutlerback.common.scroll;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ScrollRequest<C, SC> {
    private C cursor;
    private SC subCursor;

    @Builder.Default
    private int size = 10;
}
