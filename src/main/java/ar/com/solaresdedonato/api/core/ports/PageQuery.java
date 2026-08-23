package ar.com.solaresdedonato.api.core.ports;

import lombok.Getter;

@Getter
public class PageQuery {

    private static final int SIZE_MAXIMO = 100;

    private final int page;
    private final int size;

    private PageQuery(int page, int size) {
        this.page = Math.max(page, 0);
        this.size = Math.min(Math.max(size, 1), SIZE_MAXIMO);
    }

    public static PageQuery of(int page, int size) {
        return new PageQuery(page, size);
    }
}
