package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.ports.PageResult;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

/**
 * Nombres de campo alineados a la forma de {@code org.springframework.data.domain.Page}
 * ({@code number} en vez de {@code page}) porque así quedó consumido en el frontend.
 */
@Data
@Builder
public class PageDto<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;

    public static <S, T> PageDto<T> fromPageResult(PageResult<S> result, Function<S, T> mapper) {
        return PageDto.<T>builder()
                .content(result.getContent().stream().map(mapper).toList())
                .number(result.getPage())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }
}
