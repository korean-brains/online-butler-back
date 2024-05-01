package com.koreanbrains.onlinebutlerback.common.page;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
public class Page<T> {
    private final List<T> content;
    private final int number;
    private final int size;
    private final int totalPages;
    private final long totalElements;
    private final boolean isFirst;
    private final boolean hasNext;

    public Page(List<T> content, int number, int size, long totalElements) {
        this.content = content;
        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = size == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);
        this.isFirst = number == 1;
        this.hasNext = number < totalPages;
    }

    public <U> Page<U> map(Function<? super T, U> converter) {
        List<U> list = content.stream().map(converter).toList();
        return new Page<>(list, number, size, totalElements);
    }

}
