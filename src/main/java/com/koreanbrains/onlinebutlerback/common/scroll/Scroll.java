package com.koreanbrains.onlinebutlerback.common.scroll;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class Scroll<T> {
    private List<T> content;
    private Object nextCursor;
    private Object nextSubCursor;

    public <U> Scroll<U> map(Function<? super T, U> converter) {
        List<U> list = content.stream().map(converter).toList();
        return new Scroll<>(list, nextCursor, nextSubCursor);
    }
}
