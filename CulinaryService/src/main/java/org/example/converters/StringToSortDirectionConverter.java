package org.example.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class StringToSortDirectionConverter implements Converter<String, Direction> {

    @Override
    public Direction convert(String source) {
        return Direction.fromString(source.toUpperCase());
    }
}
