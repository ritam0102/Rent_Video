package com.rentvideo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RentalDto {
    private Long id;
    private String userEmail;
    private String videoTitle;
    private LocalDateTime rentedAt;
    private LocalDateTime returnedAt;
    private boolean active;
}
