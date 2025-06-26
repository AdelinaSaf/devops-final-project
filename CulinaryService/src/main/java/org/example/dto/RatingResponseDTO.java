package org.example.dto;

import lombok.Data;

@Data
public class RatingResponseDTO {
    private Double averageRating;
    private int totalRatings;
}