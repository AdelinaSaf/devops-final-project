package org.example.converters;

import org.example.dto.RatingDTO;
import org.example.dto.RatingResponseDTO;
import org.example.entity.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingConverter {

    public Rating toEntity(RatingDTO dto) {
        Rating rating = new Rating();
        rating.setRating(dto.getValue());
        return rating;
    }

    public RatingResponseDTO toResponseDTO(double averageRating, int totalRatings) {
        RatingResponseDTO dto = new RatingResponseDTO();
        dto.setAverageRating(averageRating);
        dto.setTotalRatings(totalRatings);
        return dto;
    }
}